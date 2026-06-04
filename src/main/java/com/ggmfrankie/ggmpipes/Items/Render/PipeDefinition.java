package com.ggmfrankie.ggmpipes.Items.Render;

public record PipeDefinition(
        boolean north,
        boolean south,
        boolean east,
        boolean west,
        boolean up,
        boolean down,
        String pipeType
) {}
