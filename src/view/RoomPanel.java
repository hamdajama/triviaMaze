package view;

import model.Room;
import model.Direction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.JPanel;
import java.util.EnumMap;
import java.util.Map;

public class RoomPanel extends JPanel implements PropertyChangeListener, Serializable {
    private static final long serialVersionUID = 3L;

    private Map<Direction, Boolean> doorStates;

    public RoomPanel() {
        super();
        doorStates = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            doorStates.put(dir, dir == Direction.SOUTH || dir == Direction.EAST);
        }
    }

    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        final Graphics2D graphics2D = (Graphics2D) theG;
        drawRoom(graphics2D);
    }

    private void drawRoom(final Graphics2D theGraphics2D) {
        final int width = getWidth();
        final int height = getHeight();
        final int roomSize = 40;

        theGraphics2D.setColor(Color.WHITE);
        theGraphics2D.drawRect(roomSize, roomSize, width - 2 * roomSize, height - 2 * roomSize);

        final int doorSize = 30;

        drawDoor(theGraphics2D, Direction.NORTH, width / 2 - roomSize / 2, 10, doorSize, doorSize);
        drawDoor(theGraphics2D, Direction.EAST, width - roomSize, height / 2 - roomSize / 2, doorSize, doorSize);
        drawDoor(theGraphics2D, Direction.SOUTH, width / 2 - roomSize / 2, height - roomSize, doorSize, doorSize);
        drawDoor(theGraphics2D, Direction.WEST, 10, height / 2 - roomSize / 2, doorSize, doorSize);

        drawText(theGraphics2D, width, height);
    }

    private void drawDoor(Graphics2D g, Direction dir, int x, int y, int width, int height) {
        g.setColor(doorStates.get(dir) ? Color.GREEN : Color.RED);
        g.fillRect(x, y, width, height);
    }

    private void drawText(final Graphics2D theGraphics2D, final int theWidth, final int theHeight) {
        theGraphics2D.setColor(Color.WHITE);
        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, 10));

        theGraphics2D.drawString("Move North", (theWidth / 2) - 40, 50);
        theGraphics2D.drawString("Move East", theWidth - 100, theHeight / 2);
        theGraphics2D.drawString("Move South", (theWidth / 2) - 40, 250);
        theGraphics2D.drawString("Move West", theWidth - 350, theHeight / 2);

        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, 30));
        theGraphics2D.drawString("1", (theWidth/2) - 15, theHeight/2);
    }

    public void updateRoomPanel(Room theRoom) {
        for (Direction dir : Direction.values()) {
            doorStates.put(dir, !theRoom.getDoor(dir).isClosed());
        }
        repaint();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {
        try {
            Direction dir = Direction.valueOf(theEvt.getPropertyName());
            doorStates.put(dir, !(boolean) theEvt.getNewValue());
            repaint();
        } catch (IllegalArgumentException e) {
            // Not a direction property, ignore
        }
    }
}