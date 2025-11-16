package com.mediaupload.service;

/**
 * Service interface for QR code generation.
 */
public interface QRCodeService {

    /**
     * Generate a QR code for the given data.
     *
     * @param data the data to encode in the QR code
     * @param size the size of the QR code (width and height)
     * @return Base64 encoded QR code image
     */
    String generateQRCode(String data, int size);

    /**
     * Generate a QR code with default size.
     *
     * @param data the data to encode
     * @return Base64 encoded QR code image
     */
    String generateQRCode(String data);
}
