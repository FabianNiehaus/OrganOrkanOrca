package eshop.common.exceptions;

public class BasketNonexistantException extends Exception {

    private static final long serialVersionUID = -381709339963813620L;

    public BasketNonexistantException() {
	super("Der Warenkorb existiert nicht!");
    }
}
