const express = require("express");
const crypto = require("crypto");
const sqlite3 = require("sqlite3");

const app = express();
const port = 3000;

const db = new sqlite3.Database("./aims.db");
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
  const receivedHashBuffer = Buffer.from(req.query.vnp_SecureHash, "hex");

  if (hashBuffer.equals(receivedHashBuffer)) {
    db.get(
      "SELECT * FROM 'Transaction' WHERE id = ?",
      [vnp_TxnRef],
      (err, row) => {
        if (err) {
          console.error("Error:", err.message);
          res.status(500).json({ error: "Internal Server Error" });
        } else if (row) {
          if ((vnp_Amount = row.amount)) {
            if (row.status.equals("Pending")) {
              if (vnp_ResponseCode.equals("00")) {
                updateColumnValue(
                  vnp_TxnRef,
                  "status",
                  "Order success",
                  (err, result) => {
                    if (err) {
                      console.error("Error:", err.message);
                    } else {
                      console.log(result);
                    }
                  }
                );
                const currentTime = new Date()
                  .toISOString()
                  .slice(0, 19)
                  .replace("T", " ");
                updateColumnValue(
                  vnp_TxnRef,
                  "paidAt",
                  currentTime,
                  (err, result) => {
                    if (err) {
                      console.error("Error:", err.message);
                    } else {
                      console.log(result);
                    }
                  }
                );
                res.json({ RspCode: "00", Message: "Confirm Success" });
              } else {
                updateColumnValue(
                  vnp_TxnRef,
                  "status",
                  "VNP ERROR",
                  (err, result) => {
                    if (err) {
                      console.error("Error:", err.message);
                    } else {
                      console.log(result);
                    }
                  }
                );
                res.json({ RspCode: "00", Message: "Confirm Success" });
              }
            } else {
              updateColumnValue(
                vnp_TxnRef,
                "status",
                "Order already confirmed",
                (err, result) => {
                  if (err) {
                    console.error("Error:", err.message);
                  } else {
                    console.log(result);
                  }
                }
              );
              res.json({ RspCode: "02", Message: "Order already confirmed" });
            }
          } else {
            updateColumnValue(
              vnp_TxnRef,
              "status",
              "Order failed",
              (err, result) => {
                if (err) {
                  console.error("Error:", err.message);
                } else {
                  console.log(result);
                }
              }
            );
            res.json({ RspCode: "04", Message: "Invalid amount" });
          }
        } else {
          updateColumnValue(
            vnp_TxnRef,
            "status",
            "Order not found",
            (err, result) => {
              if (err) {
                console.error("Error:", err.message);
              } else {
                console.log(result);
              }
            }
          );
          res.json({ RspCode: "01", Message: "Order not found" });
        }
      }
    );
  } else {
    res.status(400).json({ error: "Hash verification failed" });
  }
  db.close();
});

function updateColumnValue(id, columnName, columnValue, callback) {
  const query = `UPDATE 'Transaction' SET ${columnName} = ? WHERE id = ?`;

  db.run(query, [columnValue, id], function (err) {
    if (err) {
      callback(err);
    } else {
      if (this.changes > 0) {
        callback(null, `Successfully updated ${columnName} for id ${id}`);
      } else {
        callback(null, `No rows updated. No id found: ${id}`);
      }
    }
    db.close();
  });
}

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
