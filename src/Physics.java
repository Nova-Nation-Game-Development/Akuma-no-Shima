package src;

public class Physics {
    
    private static final double GRAVITY = 1;
    private static final double INITIAL_VERTICAL_VELOCITY = -12.0;
    private static final double INITIAL_HORIZONTAL_VELOCITY = 5.0;

    private static final int JUMP_INTERVAL = 200; // In milliseconds (ms)
    private static final int MAX_STEP_COUNT = 25; // Maximum steps for calculating the jump // 25 / (1000 / 200)

    public boolean isJumping = false;
    private double currTime = 0;
    private double initY = 0;
    private double initX = 0;

    // Accessors

    public static int getJumpInterval() { return JUMP_INTERVAL; }
    public static int getMaxStep() { return MAX_STEP_COUNT; }

    // Functions
    
    public void applyGravity(Entity entity, double x, double y) { }

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

    public void startJump(double startY, double startX)
    {
        isJumping = true;
        currTime = 0;
        initY = startY;
        initX = startX;
    }

    public void resetJumpPos()
    {
        isJumping = false;
        currTime = 0;
    }
}
