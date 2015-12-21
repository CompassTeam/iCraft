package iCraft.core.utils;
/**
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.EnumFacing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
*/
public class JsonUtils
{
    /**
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static JsonElement serialize(ModelBase src)
    {
        JsonObject container = new JsonObject();
        JsonArray elements = new JsonArray();

        for(ModelRenderer boxes : (List<ModelRenderer>)src.boxList)
        {
            for(ModelBox box : (List<ModelBox>)boxes.cubeList)
            {
                JsonObject element = new JsonObject();
                float x1 = 8 +  (boxes.offsetX + boxes.rotationPointX + box.posX1);
                float x2 = 8 +  (boxes.offsetX + boxes.rotationPointX + box.posX2);
                float y1 = 24 - (boxes.offsetY + boxes.rotationPointY + box.posY1);
                float y2 = 24 - (boxes.offsetY + boxes.rotationPointY + box.posY2);
                float z1 = 8 + (boxes.offsetZ + boxes.rotationPointZ + box.posZ1);
                float z2 = 8 + (boxes.offsetZ + boxes.rotationPointZ + box.posZ2);
                JsonArray from = new JsonArray();
                from.add(new JsonPrimitive(Math.min(x1, x2)));
                from.add(new JsonPrimitive(Math.min(y1, y2)));
                from.add(new JsonPrimitive(Math.min(z1, z2)));
                JsonArray to = new JsonArray();
                to.add(new JsonPrimitive(Math.max(x1, x2)));
                to.add(new JsonPrimitive(Math.max(y1, y2)));
                to.add(new JsonPrimitive(Math.max(z1, z2)));
                JsonObject faces = new JsonObject();
                JsonObject face = new JsonObject();
                face.add("texture", new JsonPrimitive("render"));
                for(EnumFacing side : EnumFacing.values())
                {
                    faces.add(side.getName(), face);
                }
                element.add("__comment", new JsonPrimitive("emptyComment"));
                element.add("from", from);
                element.add("to", to);
                element.add("faces", faces);
                elements.add(element);
            }
        }
        container.add("elements", elements);
        return container;
    }

    public static void main(String[] args)
    {
        for(String string : args)
        {
            try
            {
                Class<ModelBase> modelClass = (Class<ModelBase>)Class.forName("iCraft.client.model." + string);
                ModelBase modelInstance = modelClass.newInstance();
                FileWriter modelFile = new FileWriter("converted_models/" + string + ".json");
                JsonUtils.GSON.toJson(JsonUtils.serialize(modelInstance), modelFile);
                modelFile.close();
            } catch(ClassNotFoundException e)
            {
                e.printStackTrace();
            } catch(InstantiationException e)
            {
                e.printStackTrace();
            } catch(IllegalAccessException e)
            {
                e.printStackTrace();
            } catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
     */
}