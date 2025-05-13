package cat.exception;

/**
 * 不正なオプションが渡された場合の例外です
 * <p/>
 */
public class IllegalOptionException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public IllegalOptionException(String msg) {
        super(msg);
    }
}
