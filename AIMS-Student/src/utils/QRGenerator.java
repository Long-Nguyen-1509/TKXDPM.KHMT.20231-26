package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class QRGenerator {
    public static BufferedImage generateQRCodeImage(String text) throws Exception {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 1000, 1000, hints);

            BufferedImage bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 1000; x++) {
                for (int y = 0; y < 1000; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            return bufferedImage;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
