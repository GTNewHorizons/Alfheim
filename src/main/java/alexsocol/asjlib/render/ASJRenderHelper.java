package alexsocol.asjlib.render;
	//forum.mcmodding.ru/threads/7509/
	
	/*public static void drawTextureCustomSize(double posX, double posY, double startPixX, double startPixY, double pieceSizeX, double pieceSizeY, float sizeTextureX, float sizeTextureY) {
		float f4 = 1.0F / sizeTextureX;
		float f5 = 1.0F / sizeTextureY;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)posX, (double)(posY + pieceSizeY), 0.0, (double)(startPixX * f4), (double)((startPixY + (float)pieceSizeY) * f5));
		tessellator.addVertexWithUV((double)(posX + pieceSizeX), (double)(posY + pieceSizeY), 0.0, (double)((startPixX + (float)pieceSizeX) * f4), (double)((startPixY + (float)pieceSizeY) * f5));
		tessellator.addVertexWithUV((double)(posX + pieceSizeX), (double)posY, 0.0, (double)((startPixX + (float)pieceSizeX) * f4), (double)(startPixY * f5));
		tessellator.addVertexWithUV((double)posX, (double)posY, 0.0, (double)(startPixX * f4), (double)(startPixY * f5));
		tessellator.draw();
	}//p.s. ���� ���� ������� ������ � �����.
	
	RenderHelper.drawTextureCustomSize(0, 0, startPixX, startPixY, pieceSizeX, pieceSizeY, sizeTextureX, sizeTextureY);//������� ����� ����

	RenderHelper.drawTextureCustomSize(w - pieceSizeX, 0, startPixX, startPixY, pieceSizeX, pieceSizeY, sizeTextureX, sizeTextureY);//������� ������ ����

	RenderHelper.drawTextureCustomSize(w - pieceSizeX, h - pieceSizeY, startPixX, startPixY, pieceSizeX, pieceSizeY, sizeTextureX, sizeTextureY);//������ ������ ����

	RenderHelper.drawTextureCustomSize(0, h - pieceSizeY, startPixX, startPixY, pieceSizeX, pieceSizeY, sizeTextureX, sizeTextureY);//������ ����� ����

	RenderHelper.drawTextureCustomSize((w - pieceSizeX)/2, (h - pieceSizeY)/2, startPixX, startPixY, pieceSizeX, pieceSizeY, sizeTextureX, sizeTextureY); //�����

	{
	float scale = 0.7F //Percent scale;
	double posX = (w - pieceSizeX*scale)/scale; //������ ������� ������

	GL11.glPushMatrix();
	GL11.glScalef(scale, scale, 1);

	//Draw

	GL11.glPopMatrix();
}*/

/*

String text = "svk";

drawString(font, text, posX, posY, new Color(255, 0, 0).getRGB()); //����� ����� �������
drawString(font, text, posX - font.getStringWidth(text)/2, posY, new Color(255, 0, 0).getRGB()); //����� �� ������
drawString(font, text, posX - font.getStringWidth(text), posY, new Color(255, 0, 0).getRGB()); //����� ������ ������.

*/


/**
 * ������ �������
 * @param centerX ����������� ����� �� �����������
 * @param centerY ����������� ����� �� ���������
 * @param radius ������ ��������
 * @param segments ���������� ����� �� ����������
 * @param fillColor ���� ����������
 * @param alpha255 ����� �����
 */
/*public static void materialCircle(double centerX, double centerY, double radius, int segments, int fillColor, int alpha255) {
	GL11.glDisable(GL11.GL_TEXTURE_2D);
	GL11.glPushMatrix();
	GL11.glEnable(GL11.GL_BLEND);

	Color color = HEXtoRGB(fillColor);
	GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha255 / 255F);

	GL11.glBegin(GL11.GL_POLYGON);

	radius -=0.1F;

	for (int i = 0; i < segments; i++) {
		double rad = -2 * Math.PI * ((double) i) / ((double) segments);
		double x = Math.cos(rad);
		double y = Math.sin(rad);

		GL11.glVertex2d(x * radius + centerX + 0.5F, y * radius + centerY  + 0.5F);
	}

	GL11.glEnd();

	GL11.glDisable(GL11.GL_BLEND);
	GL11.glPopMatrix();
	GL11.glEnable(GL11.GL_TEXTURE_2D);
	GL11.glColor3f(1,1,1);
}*/

/**
 * ����� ������ �������������
 * @param posX ����� ������ �� �����������
 * @param posY ����� ������ �� ���������
 * @param width ������ ��������������
 * @param height ��� ������
 * @param hexColor ���� ������� ���� 0xFFFFFFFF.
 */
/*public static void square(double posX, double posY, double width, double height, int hexColor) {
	GL11.glDisable(GL11.GL_TEXTURE_2D);
	GL11.glPushMatrix();

	Color color = HEXtoRGB(hexColor);

	GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
	GL11.glBegin(GL11.GL_POLYGON);

	GL11.glVertex2d(posX + width, posY);
	GL11.glVertex2d(posX, posY);
	GL11.glVertex2d(posX, posY + height);
	GL11.glVertex2d(posX + width, posY + height);


	GL11.glEnd();
	GL11.glPopMatrix();
	GL11.glEnable(GL11.GL_TEXTURE_2D);
}*/