package src;

public class Physics {
    
    
    private static final double GRAVITY = 1;
   // private static final double INITIAL_VERTICAL_VELOCITY = -12.0;
    private static final double INITIAL_HORIZONTAL_VELOCITY = 5.0;
    public boolean isJumping = false;
    private double currTime = 0;
    private double initY = 0;
    private double initX = 0;
   




    public void applyGravity(Entity entity, double x, double y){

    }

    // kinematics 
    // s = ut + 1/2at^2
    public double calculateVertComponent(double initialVelocity, double timeElapsed) 
    {
        
        return(initialVelocity * timeElapsed - 0.5 * GRAVITY * timeElapsed * timeElapsed);
    }

    public double calculateHorizComponent(double direction, double timeElapsed)
    {
        return INITIAL_HORIZONTAL_VELOCITY * direction * timeElapsed;
    }

   


    public void startJump(double startY, double startX){
        isJumping = true;
        currTime = 0;
        initY = startY;
        initX = startX;
    }

    public void resetJumpPos(){
        isJumping = false;
        currTime = 0;
        
    }

    


}
