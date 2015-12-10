package iCraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.core.ICraft;

@SideOnly(Side.CLIENT)
public class GuiiCraftCalc extends GuiiCraftBase
{
	private String exNum1 = "";
	private String exNum2 = "";
	private float num1;
	private float num2;
	private static int operation = 0;

	public boolean equalsPressed = false;

	public GuiiCraftCalc(String resource)
	{
		super(resource);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
    {
		super.drawScreen(mouseX, mouseY, partialTick);

		drawString((getExResult().endsWith(".0") ? getExResult().substring(0, getExResult().indexOf(".")) : getExResult()), 146, 78, 0x404040, true, 0.5F);
		drawTime();
    }

	public String getResult()
	{
		num1 = Float.parseFloat(exNum1);
		num2 = Float.parseFloat(exNum2);
		float result;
		switch (operation)
		{
			case 1:
				result = num1 + num2;
				return Float.toString(result);
			case 2:
				result = num1 - num2;
				return Float.toString(result);
			case 3:
				result = num1 / num2;
				return Float.toString(result);
			case 4:
				result = num1 * num2;
				return Float.toString(result);
			default:
				break;
		}
		return null;
	}

	public String op(int operation)
	{
		switch (operation)
		{
			case 1:
				return "+";
			case 2:
				return "-";
			case 3:
				return "/";
			case 4:
				return "*";
			default:
				break;
		}
		return null;
	}

	public String getExResult()
	{
		return (!exNum1.equals("") ? (operation != 0 ? (!exNum2.equals("") ? (!equalsPressed ? exNum1 + op(operation) + exNum2 : getResult()) : exNum1 + op(operation)) : exNum1) : "");
	}

	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);

		if(button == 0)
		{
			int xAxis = x - guiWidth;
			int yAxis = y - guiHeight;
			//Exit
			if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
			{
				operation = 0;
				exNum1 = "";
				exNum2 = "";
				equalsPressed = false;
				mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
			}
			if (exNum1.length() + exNum2.length() < 16)
			{
				// 0
				if (xAxis >= 52 && xAxis <= 86 && yAxis >= 120 && yAxis <= 136)
				{
					if (operation == 0)
						exNum1 += "0";
					else
						exNum2 += "0";
				}
				// 1
				if (xAxis >= 52 && xAxis <= 68 && yAxis >= 102 && yAxis <= 118)
				{
					if (operation == 0)
						exNum1 += "1";
					else
						exNum2 += "1";
				}
				// 2
				if (xAxis >= 70 && xAxis <= 86 && yAxis >= 102 && yAxis <= 118)
				{
					if (operation == 0)
						exNum1 += "2";
					else
						exNum2 += "2";
				}
				// 3
				if (xAxis >= 89 && xAxis <= 105 && yAxis >= 102 && yAxis <= 118)
				{
					if (operation == 0)
						exNum1 += "3";
					else
						exNum2 += "3";
				}
				// 4
				if (xAxis >= 52 && xAxis <= 68 && yAxis >= 84 && yAxis <= 100)
				{
					if (operation == 0)
						exNum1 += "4";
					else
						exNum2 += "4";
				}
				// 5
				if (xAxis >= 70 && xAxis <= 86 && yAxis >= 84 && yAxis <= 100)
				{
					if (operation == 0)
						exNum1 += "5";
					else
						exNum2 += "5";
				}
				// 6
				if (xAxis >= 89 && xAxis <= 105 && yAxis >= 84 && yAxis <= 100)
				{
					if (operation == 0)
						exNum1 += "6";
					else
						exNum2 += "6";
				}
				// 7
				if (xAxis >= 52 && xAxis <= 68 && yAxis >= 66 && yAxis <= 82)
				{
					if (operation == 0)
						exNum1 += "7";
					else
						exNum2 += "7";
				}
				// 8
				if (xAxis >= 70 && xAxis <= 86 && yAxis >= 66 && yAxis <= 82)
				{
					if (operation == 0)
						exNum1 += "8";
					else
						exNum2 += "8";
				}
				// 9
				if (xAxis >= 89 && xAxis <= 105 && yAxis >= 66 && yAxis <= 82)
				{
					if (operation == 0)
						exNum1 += "9";
					else
						exNum2 += "9";
				}
				// Dot
				if (xAxis >= 89 && xAxis <= 105 && yAxis >= 120 && yAxis <= 136)
				{
					if (operation == 0)
					{
						if (exNum1.length() > 0 && !exNum1.contains("."))
							exNum1 += ".";
					}
					else
					{
						if (exNum2.length() > 0 && !exNum2.contains("."))
							exNum2 += ".";
					}
				}
			}
			//Plus
			if (xAxis >= 107 && xAxis <= 123 && yAxis >= 84 && yAxis <= 100)
			{
				operation = 1;
			}
			//Minus
			if (xAxis >= 107 && xAxis <= 123 && yAxis >= 66 && yAxis <= 82)
			{
				operation = 2;
			}
			//Divide
			if (xAxis >= 89 && xAxis <= 105 && yAxis >= 48 && yAxis <= 64)
			{
				operation = 3;
			}
			//Multiply
			if (xAxis >= 107 && xAxis <= 123 && yAxis >= 48 && yAxis <= 64)
			{
				operation = 4;
			}
			//+/-
			if(xAxis >= 70 && xAxis <= 86 && yAxis >= 48 && yAxis <= 64)
			{
				if (operation == 0 && !exNum1.contains("-"))
				{
					exNum1 = new StringBuffer(exNum1).insert(0, "-").toString();
				}
				else if (operation != 0 && !exNum2.contains("-"))
				{
					exNum2 = new StringBuffer(exNum2).insert(0, "-").toString();
				}
			}
			//Equals
			if(xAxis >= 107 && xAxis <= 123 && yAxis >= 102 && yAxis <= 136)
			{
				if (!exNum2.equals(""))
				{
					equalsPressed = true;
				}
			}
			//Clear
			if (xAxis >= 52 && xAxis <= 68 && yAxis >= 48 && yAxis <= 64)
			{
				operation = 0;
				exNum1 = "";
				exNum2 = "";
				equalsPressed = false;
			}
		}
	}
}