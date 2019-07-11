public class AStarException extends RuntimeException{
    private String message;

    public AStarException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
