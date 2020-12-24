package Moonworks;

import basemod.animations.AbstractAnimation;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.*;
import com.brashmonkey.spriter.LibGdx.LibGdxDrawer;
import com.brashmonkey.spriter.LibGdx.LibGdxLoader;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CustomSpriterAnimation extends SpriterAnimation {
    private static final float animFps = 0.016666668F;
    private float frameRegulator = 0.0F;
    private final LibGdxDrawer drawer;
    private final ShapeRenderer renderer = new ShapeRenderer();
    public Player myPlayer;

    public CustomSpriterAnimation(String filepath) {
        super(filepath);
        FileHandle handle = Gdx.files.internal(filepath);
        Data data = (new SCMLReader(handle.read())).getData();
        LibGdxLoader loader = new LibGdxLoader(data);
        loader.load(handle.file());
        this.drawer = new LibGdxDrawer(loader, this.renderer);
        this.myPlayer = new Player(data.getEntity(0));
        this.myPlayer.setScale(Settings.scale);
    }

    @Override //We use a different myPlayer than SpriterAnim
    public void setFlip(boolean horizontal, boolean vertical) {
        if (horizontal && this.myPlayer.flippedX() > 0 || !horizontal && this.myPlayer.flippedX() < 0) {
            this.myPlayer.flipX();
        }

        if (vertical && this.myPlayer.flippedY() > 0 || !vertical && this.myPlayer.flippedY() < 0) {
            this.myPlayer.flipY();
        }

    }

    @Override //We use an actual Player not PlayerTweener
    public void renderSprite(SpriteBatch batch, float x, float y) {
        this.drawer.batch = batch;

        for(this.frameRegulator += Gdx.graphics.getDeltaTime(); this.frameRegulator - 0.016666668F >= 0.0F; this.frameRegulator -= 0.016666668F) {
            this.myPlayer.update();
        }

        AbstractPlayer player = AbstractDungeon.player;
        if (player != null) {
            this.myPlayer.setPosition(new Point(x, y));
            this.drawer.draw(this.myPlayer);
            if (drawBones) {
                batch.end();
                this.renderer.setAutoShapeType(true);
                this.renderer.begin();
                this.drawer.drawBoxes(this.myPlayer);
                this.drawer.drawBones(this.myPlayer);
                this.renderer.end();
                batch.begin();
            }
        }

    }
}
