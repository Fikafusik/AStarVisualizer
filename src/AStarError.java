public class AStarError extends Error {
    private String message;
    public AStarError(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
