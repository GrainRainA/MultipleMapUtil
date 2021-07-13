package com.grain.map.Utils.KML;

import com.grain.map.Entity.LatLng;
import com.grain.utils.Convert;
import com.grain.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @anthor GrainRain
 * @funcation KML文件解析工具
 * @date 2021/1/21
 */
public class ParsingKmlUtil {

    public static List<LatLng> getLatLng(File file) {

        //解析的节点名
        List<String> nodeStrList = new ArrayList();
        nodeStrList.add("LineString");

        List<String> latLngStrList = creatNodeList(file, nodeStrList);
        List<LatLng> latLngList = new ArrayList<>();

        if(latLngStrList != null) {
            for (int i = 0; i < latLngStrList.size(); i++) {

                String[] splitStrList = StringUtils.split(latLngStrList.get(i), "\n");

                for (int j = 0; j < splitStrList.length; j++) {
                    String[] latLngs = StringUtils.split(splitStrList[j], ",");

                    if(latLngs.length > 1) {

                        double lng = Convert.convertToDouble(latLngs[0], 0);
                        double lat = Convert.convertToDouble(latLngs[1], 0);
                        if(lat != 0 && lng != 0) {
                            LatLng latLng = new LatLng();
                            latLng.latitude = lat;
                            latLng.longitude = lng;

                            latLngList.add(latLng);
                        }
                    }
                }
            }
        }
        return latLngList;
    }

    public static List<String> creatNodeList(File file, List<String> nodeStrList) {

        JSONObject jsonObject = new JSONObject();
        List<String> latLngStrList = new ArrayList<>();

        for (int i = 0; i < nodeStrList.size(); i++) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            NodeList nodeList = null;
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document d = builder.parse(file);
                nodeList = d.getElementsByTagName(nodeStrList.get(i));
                JSONObject jsonTemp = node(nodeList);

                try {
                    String coordinates = jsonTemp.optString("coordinates");
                    latLngStrList.add(coordinates);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                jsonObject.put(i+"", jsonTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return latLngStrList;
    }

    public static JSONObject node(NodeList list) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        jsonObject.put(childNodes.item(j).getNodeName(), childNodes.item(j).getFirstChild().getNodeValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jsonObject;
    }
}
