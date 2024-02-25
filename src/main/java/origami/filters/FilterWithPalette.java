package origami.filters;

import origami.colors.Palette;

public abstract class FilterWithPalette {

    public FilterWithPalette() {
        this.setPaletteName("sunrise");
    }

    public String paletteName;
    public boolean paletteReversed = false;

    public Palette palette;

    public void setPaletteName(String name) {
        this.paletteName = name;
        palette = new Palette(paletteName, paletteReversed);
    }

    public boolean isPaletteReversed() {
        return paletteReversed;
    }

    public void setPaletteReversed(boolean paletteReversed) {
        this.paletteReversed = paletteReversed;
        palette = new Palette(paletteName, paletteReversed);
    }

    public String getPaletteName() {
        return paletteName;
    }
}
