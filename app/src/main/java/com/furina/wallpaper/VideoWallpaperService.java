package com.furina.wallpaper;

import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class VideoWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    class VideoEngine extends Engine {
        private MediaPlayer mediaPlayer;

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            startVideo(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            releasePlayer();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                if (mediaPlayer == null) {
                    startVideo(getSurfaceHolder());
                } else {
                    try {
                        mediaPlayer.start();
                    } catch (Exception ignored) {}
                }
            } else {
                if (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                    } catch (Exception ignored) {}
                }
            }
        }

        private void startVideo(SurfaceHolder holder) {
            try {
                releasePlayer();
                mediaPlayer = MediaPlayer.create(VideoWallpaperService.this, R.raw.wallpaper);
                if (mediaPlayer == null) return;
                mediaPlayer.setSurface(holder.getSurface());
                mediaPlayer.setLooping(true);
                mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void releasePlayer() {
            if (mediaPlayer != null) {
                try {
                    if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                    mediaPlayer.release();
                } catch (Exception ignored) {}
                mediaPlayer = null;
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            releasePlayer();
        }
    }
}
