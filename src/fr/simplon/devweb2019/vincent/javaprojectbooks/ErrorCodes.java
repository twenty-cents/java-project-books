package fr.simplon.devweb2019.vincent.javaprojectbooks;

/**
 * Liste énuméré des codes erreurs applicatifs
 */
public enum ErrorCodes {
    CANNONICAL_PATH_RESOLVE_ERROR(001),
    PREPROCESS_FILE_ERROR(002);

    private int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
