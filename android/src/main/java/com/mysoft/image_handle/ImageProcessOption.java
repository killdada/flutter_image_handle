package com.mysoft.image_handle;

import java.util.List;

/**
 * Created by Zourw on 2019/6/2.
 */
public class ImageProcessOption {
    private boolean saveToAlbum = false;
    private float ratio = 0.8f;
    private int width = 1080;
    private int height = 1920;
    private boolean keepOrigin = false;
    private WatermarkConfig watermarkConfig;

    public boolean isSaveToAlbum() {
        return saveToAlbum;
    }

    public void setSaveToAlbum(boolean saveToAlbum) {
        this.saveToAlbum = saveToAlbum;
    }

    public float getRatio() {
        return (ratio <= 0.f || ratio > 1.f) ? 0.8f : ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getWidth() {
        return width <= 0 ? 1080 : width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height <= 0 ? 1920 : height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isKeepOrigin() {
        return keepOrigin;
    }

    public void setKeepOrigin(boolean keepOrigin) {
        this.keepOrigin = keepOrigin;
    }

    public WatermarkConfig getWatermarkConfig() {
        return watermarkConfig;
    }

    public void setWatermarkConfig(WatermarkConfig watermarkConfig) {
        this.watermarkConfig = watermarkConfig;
    }

    public static class WatermarkConfig {
        /**
         * textBackground : #40000000
         * textAlignment : left
         * boardPosition : bottom
         * textColunms : [{"iconPath":"","text":"","textColor":"#FFFFFF"}]
         */

        public enum TextAlignment {
            left, center, right,
        }

        public enum BoardPosition {
            top, center, bottom,
        }

        private String textBackground;
        private String textAlignment;
        private String boardPosition;
        private List<TextColumn> textColumns;

        public String getTextBackground() {
            return textBackground;
        }

        public void setTextBackground(String textBackground) {
            this.textBackground = textBackground;
        }

        public TextAlignment getTextAlignment() {
            return TextAlignment.valueOf(textAlignment);
        }

        public void setTextAlignment(String textAlignment) {
            this.textAlignment = textAlignment;
        }

        public BoardPosition getBoardPosition() {
            return BoardPosition.valueOf(boardPosition);
        }

        public void setBoardPosition(String boardPosition) {
            this.boardPosition = boardPosition;
        }

        public List<TextColumn> getTextColumns() {
            return textColumns;
        }

        public void setTextColumns(List<TextColumn> textColumns) {
            this.textColumns = textColumns;
        }

        public static class TextColumn {
            private String iconPath;
            private String text;
            private String textColor = "#FFFFFF";

            public String getIconPath() {
                return iconPath;
            }

            public void setIconPath(String iconPath) {
                this.iconPath = iconPath;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTextColor() {
                return textColor;
            }

            public void setTextColor(String textColor) {
                this.textColor = textColor;
            }
        }
    }
}
