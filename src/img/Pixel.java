package img;

public class Pixel {
    int x;
    int y;

    public Pixel(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public Pixel() {
        x = 0;
        y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pixel temp = (Pixel) obj;
        return ((this.getX() == temp.getX()) && (this.getY() == temp.getY()));
    }
}
