package physigraph;

/**
 * Created by cfy on 15-11-30.
 */
public class ArrowedDoubleLine extends DoubleLine{
    protected float arrowPosition;
    protected float arrowHeight,arrowWidth;
    public ArrowedDoubleLine(float x1,float y1,float x2,float y2){
        super(x1,y1,x2,y2);
        arrowPosition = 0.5f;
        arrowHeight = 5f;
        arrowWidth = 10;
    }

    public ArrowedDoubleLine(){
        this(0,0,0,0);
    }

    @Override
    protected void GeneratePath() {
        super.GeneratePath();
        switch (shape){
            case LINE:
                float distance = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                float k1x = (x2 - x1) / distance;
                float k1y = (y2 - y1) / distance;
                float t1x = -(y2 - y1) / distance;
                float t1y = (x2 - x1) / distance;
                p.moveTo(x1 + (x2 - x1) * arrowPosition, y1 + (y2 - y1) * arrowPosition);
                p.lineTo(x1 + (x2 - x1) * arrowPosition - (k1x * arrowWidth + t1x * arrowHeight), y1 + (y2 - y1) * arrowPosition - (k1y * arrowWidth + t1y * arrowHeight));
                p.moveTo(x1 + (x2 - x1) * arrowPosition, y1 + (y2 - y1) * arrowPosition);
                p.lineTo(x1 + (x2 - x1) * arrowPosition - (k1x * arrowWidth - t1x * arrowHeight), y1 + (y2 - y1) * arrowPosition - (k1y * arrowWidth - t1y * arrowHeight));

                break;
            case ARC:
                float length = distance(x1,y1,x2,y2);
                float radius = (float)Math.sqrt(this.radius * this.radius + length * length / 4);
                t1x = -(y2 - y1) / length;
                t1y = (x2 - x1) / length;
                float centreX = (x2 + x1) / 2 + this.radius * t1x;
                float centreY = (y2 + y1) / 2 + this.radius * t1y;
                float mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,this.radius);
                float vectorX = (x1 - centreX) * (float)Math.cos(mtheta * arrowPosition) + (y1 - centreY) * (float)Math.sin(mtheta * arrowPosition);
                float vectorY =-(x1 - centreX) * (float)Math.sin(mtheta * arrowPosition) + (y1 - centreY) * (float)Math.cos(mtheta * arrowPosition);
                vectorX /= radius;
                vectorY /= radius;
                float v2x = vectorY;
                float v2y = -vectorX;
                p.moveTo(centreX + vectorX * radius,centreY + vectorY * radius);
                p.lineTo(centreX + vectorX * (radius - arrowHeight) - v2x * arrowWidth, centreY + vectorY * (radius - arrowHeight) - v2y * arrowWidth);
                p.moveTo(centreX + vectorX * radius, centreY + vectorY * radius);
                p.lineTo(centreX + vectorX * (radius + arrowHeight) - v2x * arrowWidth,centreY + vectorY * (radius + arrowHeight) - v2y * arrowWidth);

                break;
            case LOOP:

                break;
        }
    }
}
