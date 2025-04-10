package src;

public class Physics {
    
    private static final double GRAVITY = 1;
    private static final double INITIAL_VERTICAL_VELOCITY = -12.0;
    private static final double INITIAL_HORIZONTAL_VELOCITY = 5.0;

    private static final int JUMP_INTERVAL = 5; // In milliseconds (ms)
    private static final int MAX_STEP_COUNT = 120; // Maximum steps for calculating the jump
    private static final double COUNT_SCALE = 0.3f;
    private static final double SPEED_SCALE = 0.2f;

    private static double gravity = 0.1f; // gravity = 9.8 m/s

    public boolean isJumping = false;
    private double currTime = 0;
    private double initY = 0;
    private double initX = 0;

    // Accessors

    public static int getJumpInterval() { return JUMP_INTERVAL; }
    public static int getMaxStep() { return MAX_STEP_COUNT; }
    public static double getCountScale() { return COUNT_SCALE; }
    public static double getSpeedScale() { return SPEED_SCALE; }
    public static double getGravity() { return gravity; }

    // Functions
    
    public void applyGravity(Entity entity, double x, double y) { }

    // kinematics 
    // s = ut + 1/2at^2
    public static double calculateVertComponent(double initialVelocity, double timeElapsed) 
    {
        return(initialVelocity * timeElapsed - 0.5 * GRAVITY * timeElapsed * timeElapsed);
    }

    public static double calculateHorizComponent(double direction, double timeElapsed)
    {
        return INITIAL_HORIZONTAL_VELOCITY * direction + timeElapsed;
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
