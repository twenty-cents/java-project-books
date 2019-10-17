/**
 * Classe d'exception de l'application
 *
 * NOTA : a priori, fausse bonne idée, car pas vraiment de plus value à ajouter
 * liées à ces exceptions applicatives, j'aurai pu rester sur les exceptions standard (FileNotFoundException...)

 * -> C'est juste pour l'exercice.
 */
public class BookStatisticsException extends Exception {

    // Liste énumérée des codes erreurs de l'application
    private Integer errorCode;

    public BookStatisticsException(String message) {
        super(message);
    }

    public BookStatisticsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookStatisticsException(String message, Throwable cause, ErrorCodes errorCode) {
        super(message, cause);
        this.errorCode = errorCode.getCode();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
