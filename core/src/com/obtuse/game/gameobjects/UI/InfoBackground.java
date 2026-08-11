package com.obtuse.game.gameobjects.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.BasicObject;

public abstract class InfoBackground extends BasicObject {

    public InfoBackground(String name, float defaultFD) {
        super();
        path += "UI/info/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }

    /**
     * Place this tooltip box next to the pointer (mouse on desktop, the held touch on mobile —
     * both come from Gdx.input.getX/getY), offset so it doesn't sit under the cursor, and clamped
     * to stay fully on screen. Call this right after create()/setSize() and BEFORE positioning the
     * info labels, since they are laid out relative to this box's x/y. Screen-space info stage.
     */
    public void positionAtPointer(Stage stage) {
        Vector2 v = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        stage.screenToStageCoordinates(v);
        float pad = Math.min(getWidth(), getHeight()) * 0.2f;
        float x = v.x + pad;                  // to the right of the pointer
        float y = v.y - getHeight() - pad;    // box grows upward from y, so drop it below the pointer
        // Keep the whole box (which also draws its title just above the top edge) on screen.
        float margin = getHeight() * 0.12f;
        x = Math.max(margin, Math.min(x, Obtuse.width - getWidth() - margin));
        y = Math.max(margin, Math.min(y, Obtuse.height - getHeight() - margin));
        setPosition(x, y);
    }

    public float play(String name) {
        float f = play(name,0);
        play("default",0,f - 0.02f);
        return f;
    }

    public void create(float x, float y, float width, float height) {
        setWidth(width);
        setHeight(height);
        setPosition(x, y);
    }

    public void create(float x, float y) {
        create(x, y, getWidth(), getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // Gold frame around the tooltip window itself.
        float t = Math.min(getWidth(), getHeight()) * 0.03f;
        Border.drawRect(batch, getX(), getY(), getWidth(), getHeight(), t, Border.GOLD, parentAlpha);
    }
}
