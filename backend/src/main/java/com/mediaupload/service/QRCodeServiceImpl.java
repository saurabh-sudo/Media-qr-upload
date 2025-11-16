package com.mediaupload.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mediaupload.exception.QRCodeGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of QRCodeService using ZXing library.
 */
@Service
public class QRCodeServiceImpl implements QRCodeService {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeServiceImpl.class);

    @Value("${app.qr-code.size:300}")
    private int defaultSize;

    @Value("${app.qr-code.format:PNG}")
    private String imageFormat;

    @Override
    public String generateQRCode(String data, int size) {
        try {
            logger.debug("Generating QR code for data: {}, size: {}", data, size);

            // Configure QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size, hints);

            // Convert to image
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Convert to Base64
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(qrImage, imageFormat, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            String qrCodeData = String.format("data:image/%s;base64,%s",
                    imageFormat.toLowerCase(), base64Image);

            logger.info("QR code generated successfully, size: {} bytes", imageBytes.length);
            return qrCodeData;

        } catch (WriterException e) {
            logger.error("Failed to encode QR code: {}", e.getMessage(), e);
            throw new QRCodeGenerationException("Failed to encode QR code", e);
        } catch (IOException e) {
            logger.error("Failed to write QR code image: {}", e.getMessage(), e);
            throw new QRCodeGenerationException("Failed to write QR code image", e);
        }
    }

    @Override
    public String generateQRCode(String data) {
        return generateQRCode(data, defaultSize);
    }
}
