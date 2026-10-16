package com.postyizhan.shieldmite.client;

import com.postyizhan.shieldmite.item.ShieldItem;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.minecraft.Tessellator;
import org.lwjgl.opengl.GL11;

public final class ShieldModelRenderer {
    private ShieldModelRenderer() {
    }

    public static void render(ShieldItem shield) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(shield.getModelTexture()));
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_CURRENT_BIT);
        // Match the vanilla shield model's coordinate convention: model Y/Z
        // axes are inverted when the model is submitted to the item renderer.
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        // The held model is visible from both hands and from both sides.
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        drawCuboid(tessellator, -6, -11, -2, 6, 11, -1, 0, 0);
        drawCuboid(tessellator, -1, -3, -1, 1, 3, 5, 26, 0);
        tessellator.draw();

        GL11.glPopAttrib();
    }

    private static void drawCuboid(
        Tessellator tessellator,
        double x0,
        double y0,
        double z0,
        double x1,
        double y1,
        double z1,
        double textureU,
        double textureV) {
        double width = x1 - x0;
        double height = y1 - y0;
        double depth = z1 - z0;
        double u0 = textureU;
        double v0 = textureV;

        // Keep the same vertex order and UV layout as ModelBox.  In
        // particular, top and bottom must use four distinct vertices; the
        // old two-corner helper collapsed those faces to zero area.
        quad(tessellator,
            x1, y0, z1, x1, y0, z0, x1, y1, z0, x1, y1, z1,
            u0 + depth + width, v0 + depth,
            u0 + depth + width + depth, v0 + depth + height, 1, 0, 0);
        quad(tessellator,
            x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0,
            u0, v0 + depth, u0 + depth, v0 + depth + height, -1, 0, 0);
        quad(tessellator,
            x1, y0, z1, x0, y0, z1, x0, y0, z0, x1, y0, z0,
            u0 + depth, v0, u0 + depth + width, v0 + depth, 0, -1, 0);
        quad(tessellator,
            x1, y1, z0, x0, y1, z0, x0, y1, z1, x1, y1, z1,
            u0 + depth + width, v0,
            u0 + depth + width + width, v0 + depth, 0, 1, 0);
        quad(tessellator,
            x1, y0, z0, x0, y0, z0, x0, y1, z0, x1, y1, z0,
            u0 + depth, v0 + depth,
            u0 + depth + width, v0 + depth + height, 0, 0, -1);
        quad(tessellator,
            x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1,
            u0 + depth + width + depth, v0 + depth,
            u0 + depth + width + depth + width, v0 + depth + height, 0, 0, 1);
    }

    private static void quad(
        Tessellator tessellator,
        double ax, double ay, double az,
        double bx, double by, double bz,
        double cx, double cy, double cz,
        double dx, double dy, double dz,
        double u0,
        double v0,
        double u1,
        double v1,
        float nx,
        float ny,
        float nz) {
        double scale = 1.0D / 64.0D;
        tessellator.setNormal(nx, ny, nz);
        tessellator.addVertexWithUV(ax, ay, az, u0 * scale, v0 * scale);
        tessellator.addVertexWithUV(bx, by, bz, u0 * scale, v1 * scale);
        tessellator.addVertexWithUV(cx, cy, cz, u1 * scale, v1 * scale);
        tessellator.addVertexWithUV(dx, dy, dz, u1 * scale, v0 * scale);
    }
}
