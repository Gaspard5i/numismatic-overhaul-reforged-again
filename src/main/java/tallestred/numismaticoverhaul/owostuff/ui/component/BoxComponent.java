package tallestred.numismaticoverhaul.owostuff.ui.component;

import tallestred.numismaticoverhaul.owostuff.ui.base.BaseComponent;
import tallestred.numismaticoverhaul.owostuff.ui.core.AnimatableProperty;
import tallestred.numismaticoverhaul.owostuff.ui.core.Color;
import tallestred.numismaticoverhaul.owostuff.ui.core.Sizing;
import tallestred.numismaticoverhaul.owostuff.ui.parsing.UIModel;
import tallestred.numismaticoverhaul.owostuff.ui.parsing.UIParsing;
import tallestred.numismaticoverhaul.owostuff.ui.util.Drawer;
import org.w3c.dom.Element;

import java.util.Map;

public class BoxComponent extends BaseComponent {

    protected boolean fill = false;
    protected GradientDirection direction = GradientDirection.TOP_TO_BOTTOM;

    protected AnimatableProperty<Color> startColor = AnimatableProperty.of(Color.BLACK);
    protected AnimatableProperty<Color> endColor = AnimatableProperty.of(Color.BLACK);

    public BoxComponent(Sizing horizontalSizing, Sizing verticalSizing) {
        this.sizing(horizontalSizing, verticalSizing);
    }

    @Override
    public void update(float delta, int mouseX, int mouseY) {
        super.update(delta, mouseX, mouseY);
        this.startColor.update(delta);
        this.endColor.update(delta);
    }

    @Override
    public void draw(Drawer matrices, int mouseX, int mouseY, float partialTicks, float delta) {
        final int startColor = this.startColor.get().argb();
        final int endColor = this.endColor.get().argb();

        if (this.fill) {
            switch (this.direction) {
                case TOP_TO_BOTTOM -> matrices.drawGradientRect(this.x, this.y, this.width, this.height,
                        startColor, startColor, endColor, endColor);
                case RIGHT_TO_LEFT -> matrices.drawGradientRect(this.x, this.y, this.width, this.height,
                        endColor, startColor, startColor, endColor);
                case BOTTOM_TO_TOP -> matrices.drawGradientRect(this.x, this.y, this.width, this.height,
                        endColor, endColor, startColor, startColor);
                case LEFT_TO_RIGHT -> matrices.drawGradientRect(this.x, this.y, this.width, this.height,
                        startColor, endColor, endColor, startColor);
            }
        } else {
            matrices.drawGradientRect(this.x, this.y, this.width, this.height, startColor, endColor, endColor, startColor);
        }
    }

    public BoxComponent fill(boolean fill) {
        this.fill = fill;
        return this;
    }

    public boolean fill() {
        return this.fill;
    }

    public BoxComponent direction(GradientDirection direction) {
        this.direction = direction;
        return this;
    }

    public GradientDirection direction() {
        return this.direction;
    }

    public BoxComponent color(Color color) {
        this.startColor.set(color);
        this.endColor.set(color);
        return this;
    }

    public BoxComponent startColor(Color startColor) {
        this.startColor.set(startColor);
        return this;
    }

    public AnimatableProperty<Color> startColor() {
        return this.startColor;
    }

    public BoxComponent endColor(Color endColor) {
        this.endColor.set(endColor);
        return this;
    }

    public AnimatableProperty<Color> endColor() {
        return this.endColor;
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);

        UIParsing.expectChildren(element, children, "sizing");

        UIParsing.apply(children, "color", Color::parse, this::color);
        UIParsing.apply(children, "start-color", Color::parse, this::startColor);
        UIParsing.apply(children, "end-color", Color::parse, this::endColor);
        UIParsing.apply(children, "fill", UIParsing::parseBool, this::fill);
        UIParsing.apply(children, "direction", UIParsing.parseEnum(GradientDirection.class), this::direction);
    }

    public enum GradientDirection {
        TOP_TO_BOTTOM, /*TOP_LEFT_TO_BOTTOM_RIGHT,*/
        RIGHT_TO_LEFT, /*TOP_RIGHT_TO_BOTTOM_LEFT,*/
        BOTTOM_TO_TOP, /*BOTTOM_RIGHT_TO_TOP_LEFT,*/
        LEFT_TO_RIGHT, /*BOTTOM_LEFT_TO_TOP_RIGHT*/
    }
}
