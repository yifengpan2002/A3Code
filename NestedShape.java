/*
 *  ============================================================================================
 *  Describe the shape for nestedshape, the constructor and all relation function about 
 * nestedshape.
 *  YOUR UPI: ypan179
 *  ============================================================================================
 */
import java.awt.Color;
import java.util.*;
class NestedShape extends RectangleShape{
    private ArrayList<Shape> innerShapes = new ArrayList<Shape>();
    
    public NestedShape(){
        super();
        createInnerShape(0,0,width/2,height/2,color, PathType.BOUNCE, text, ShapeType.RECTANGLE);
    }
    
    public NestedShape(int x, int y, int w, int h, int mw, int mh, Color c, PathType pt, String t){
        super(x ,y ,w, h ,mw ,mh, c, pt, t);
        createInnerShape(0,0,w/2,h/2,c, PathType.BOUNCE, t, ShapeType.RECTANGLE);
    }
    
    public NestedShape(int width, int height){
        super(0, 0, width, height, DEFAULT_MARGIN_WIDTH, DEFAULT_MARGIN_HEIGHT, Color.black,PathType.BOUNCE, "");
    }
    
    public Shape createInnerShape(int x, int y, int w, int h, Color c, PathType pt,String text, ShapeType st){
        Shape inner_shape;
        if (st == ShapeType.RECTANGLE){
                 inner_shape = new RectangleShape(x ,y ,w, h ,width ,height, c, pt, text);
        }else if(st == ShapeType.OVAL){
                 inner_shape = new OvalShape(x ,y ,w, h ,width ,height, c, pt, text);
        }else{
                 inner_shape = new NestedShape(x ,y ,w, h ,width ,height, c, pt, text);
        }//finish creating inner shape; 2: need to add it into the list
        innerShapes.add(inner_shape);
        inner_shape.setParent(this);
        return inner_shape;
    }
    public void remove(Shape s){
        innerShapes.remove(s);
        s.setParent(null);
    }
    public ArrayList<Shape> getAllInnerShapes(){
        return innerShapes;
    }
    public void add(Shape s){
        int index = innerShapes.size();// I am assuming that we are adding it to the last index element
        innerShapes.add(s);
        s.setParent(this);
    }
    public int indexOf(Shape s){
        return innerShapes.indexOf(s);
    }
    public Shape getInnerShapeAt(int index){
        return innerShapes.get(index);
    }
    public int getSize(){
        return innerShapes.size();
    }
    
    public void setWidth(int w){
        this.width = w;
        for (int i = 0; i < innerShapes.size();i++){
            innerShapes.get(i).marginWidth = w;
        }
    }
    public void setHeight(int h){
        this.height = h;
        for (int i = 0; i < innerShapes.size();i++){
            innerShapes.get(i).marginHeight = h;
        }
    }
    public void setColor(Color c){
        this.color = c;
        for (int i = 0; i < innerShapes.size();i++){
            innerShapes.get(i).color = c;
        }
    }
    public void setText(String t){
        this.text = t;
        for (int i = 0; i < innerShapes.size();i++){
            innerShapes.get(i).text = t;
        }
    }
    public void draw(Painter painter){
        painter.setPaint(Color.black);
        //setColor(color);
        painter.drawRect(getX(), getY(), getWidth(), getHeight());
        painter.translate(getX(),getY());
        for (int i = 0; i < innerShapes.size();i++){
            Shape s = getInnerShapeAt(i);
            //this.setColor(s.getColor());
            //painter.translate(s.getX(),s.getY());
            s.draw(painter);
            //painter.translate(-(s.getX()),-(s.getY()));
        }
        painter.translate(-(getX()),-(getY()));
    }
    public void move(){
        this.path.move();
        for(int i = 0; i< getSize(); i++){
            Shape s = getInnerShapeAt(i);
            s.path.move();
        }
    }
    
}