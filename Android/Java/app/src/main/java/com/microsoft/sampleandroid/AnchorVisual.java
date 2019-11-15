// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.
package com.microsoft.sampleandroid;

import android.net.Uri;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.microsoft.azure.spatialanchors.CloudSpatialAnchor;

import static android.content.ContentValues.TAG;

class AnchorVisual {
    private final AnchorNode anchorNode;
    private CloudSpatialAnchor cloudAnchor;
    private Material color;
    private Renderable nodeRenderable;

    public AnchorVisual(Anchor localAnchor) {
        anchorNode = new AnchorNode(localAnchor);
    }

    public AnchorNode getAnchorNode() {
        return this.anchorNode;
    }

    public CloudSpatialAnchor getCloudAnchor() {
        return this.cloudAnchor;
    }

    public Anchor getLocalAnchor() {
        return this.anchorNode.getAnchor();
    }

    public void render(ArFragment arFragment) {
        MainThreadContext.runOnUiThread(() -> {
            ModelRenderable.builder()
                    // To load asf an asset from the 'assets' folder ('src/main/assets/andy.sfb'):
                    .setSource(arFragment.getContext(), R.raw.andy)
                    .build()
                    .thenAccept(renderable ->{
                        MainThreadContext.runOnUiThread(() -> {
                            anchorNode.setRenderable(renderable);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());

                            TransformableNode thing = new TransformableNode(arFragment.getTransformationSystem());
                            thing.setParent(this.anchorNode);
                            thing.setRenderable(this.nodeRenderable);
                            thing.select();
                        });
                    })
                    .exceptionally(
                            throwable -> {
                                Log.e(TAG, "Unable to load Renderable.", throwable);
                                return null;
                            });

        });
    }

    public void setCloudAnchor(CloudSpatialAnchor cloudAnchor) {
        this.cloudAnchor = cloudAnchor;
    }

    public void setColor(Material material) {

    }

    public void destroy() {
        MainThreadContext.runOnUiThread(() -> {
            anchorNode.setRenderable(null);
            anchorNode.setParent(null);
        });
    }
}
