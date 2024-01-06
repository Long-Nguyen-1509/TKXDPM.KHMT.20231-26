const express = require("express");
const crypto = require("crypto");
const sqlite3 = require("sqlite3");
const bodyParser = require("body-parser");

const app = express();
const port = 3000;
const db = new sqlite3.Database("./aims.db");
app.use(bodyParser.json());
app.use(express.json());

app.get("/api/ipn", (req, res) => {
  console.log(req.url);
  const {
    vnp_Amount,
    vnp_BankCode,
    vnp_BankTranNo,
    vnp_CardType,
    vnp_OrderInfo,
    vnp_PayDate,
    vnp_ResponseCode,
    vnp_TmnCode,
    vnp_TransactionNo,
    vnp_TransactionStatus,
    vnp_TxnRef,
    vnp_SecureHash,
  } = req.query;

  const dataString = req.url.split("?")[1];

  const dataToHash = dataString.split("&vnp_SecureHash")[0];

  const secretKey = "KKGIASHWQGOCMUWAQWIHKYTNZPSWURZN";

  const hmac = crypto.createHmac("sha512", secretKey);
  hmac.update(dataToHash);

  const hash = hmac.digest("hex");

  const hashBuffer = Buffer.from(hash, "hex");
  const receivedHashBuffer = Buffer.from(vnp_SecureHash, "hex");
  console.log(vnp_TxnRef);
  if (hashBuffer.equals(receivedHashBuffer)) {
    db.get(
      "SELECT * FROM 'Transaction' WHERE id = ?",
      [parseInt(vnp_TxnRef, 10)],
      (err, row) => {
        if (err) {
          console.error("Error querying database:", err.message);
          res.status(500).json({ error: "Internal Server Error" });
          return;
        } else if (row) {
          if (parseInt(vnp_Amount, 10) === row.amount) {
            if (row.status === "Pending") {
              if (vnp_ResponseCode === "00") {
                const currentTime = new Date()
                  .toISOString()
                  .slice(0, 19)
                  .replace("T", " ");
                updateColumns(
                  vnp_TxnRef,
                  {
                    status: "Order success",
                  },
                  (updateErr, result) => {
                    if (updateErr) {
                      console.error(
                        "Error updating columns:",
                        updateErr.message
                      );
                    } else {
                      console.log(result);
                    }
                  }
                );
                res.json({ RspCode: "00", Message: "Confirm Success" });
              } else {
                // Update status to "VNP ERROR"
                updateColumns(
                  vnp_TxnRef,
                  {
                    status: "VNP rrror",
                  },
                  (updateErr, result) => {
                    if (updateErr) {
                      console.error(
                        "Error updating columns:",
                        updateErr.message
                      );
                    } else {
                      console.log(result);
                    }
                  }
                );
                res.json({ RspCode: "00", Message: "Confirm Success" });
              }
            } else {
              // Order already confirmed
              res.json({ RspCode: "02", Message: "Order already confirmed" });
            }
          } else {
            // Invalid amount
            updateColumns(
              vnp_TxnRef,
              {
                status: "Order failed",
              },
              (updateErr, result) => {
                if (updateErr) {
                  console.error("Error updating columns:", updateErr.message);
                } else {
                  console.log(result);
                }
              }
            );
            res.json({ RspCode: "04", Message: "Invalid amount" });
          }
        } else {
          // Order not found
          res.json({ RspCode: "01", Message: "Order not found" });
        }
      }
    );
  } else {
    res.status(400).json({ error: "Hash verification failed" });
  }
});

app.post("/api/add-transaction", (req, res) => {
  const { id, amount } = req.body;
  db.run(
    "INSERT INTO 'Transaction' (id, amount, status) VALUES (?, ?, ?)",
    [id, amount, "Pending"],
    function (err) {
      if (err) {
        console.error(err.message);
      } else {
        res.status(201).json({ message: "ok" });
        console.log(`Row inserted with ID: ${id}`);
      }
    }
  );
});

app.get("/api/check-status", (req, res) => {
  const id = req.query.id;

  db.get(
    "SELECT status FROM 'Transaction' WHERE id = ?",
    [id],
    function (err, row) {
      if (err) {
        console.error(err.message);
      } else {
        res.status(200).json({ status: row.status });
      }
    }
  );
});

function updateColumns(id, updates, callback) {
  const setClauses = Object.keys(updates)
    .map((column) => `${column} = ?`)
    .join(", ");
  const values = Object.values(updates);

  const query = `UPDATE 'Transaction' SET ${setClauses} WHERE id = ?`;

  db.run(query, [...values, id], function (err) {
    if (err) {
      callback(err);
    } else {
      if (this.changes > 0) {
        callback(null, `Successfully updated columns for id ${id}`);
      } else {
        callback(null, `No rows updated. No id found: ${id}`);
      }
    }
  });
}

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
