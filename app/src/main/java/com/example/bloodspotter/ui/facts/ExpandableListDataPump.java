package com.example.bloodspotter.ui.facts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Facts_about_blood_needs = new ArrayList<String>();
        Facts_about_blood_needs.add("Every year our nation requires about 5 crore units of blood,out of which only a meager 80 Lakh units of blood are available.");
        Facts_about_blood_needs.add("The gift of blood is the gift of life. There is no substitute for human blood.");
        Facts_about_blood_needs.add("Every two seconds someone needs blood.");
        Facts_about_blood_needs.add("More than 38,000 blood donations are needed every  day.");
        Facts_about_blood_needs.add("A total of 30 million blood components are transfused each year.");
        Facts_about_blood_needs.add("The average red blood cell transfusion is approximately 3 pints.");
        Facts_about_blood_needs.add("The blood type most often requested by hospitals is Type O.");
        Facts_about_blood_needs.add("Sickle cell patients can require frequent blood transfusions throught thier lives.");
        Facts_about_blood_needs.add("More than 1 million new people are diagnosed with cancer each year. Many of them will need blood, sometimes daily, during their chemotherapy treatment.");
        Facts_about_blood_needs.add("A single car accident victim can require as many as 100 units of blood.");

        List<String> Facts_about_the_blood_supply = new ArrayList<String>();
        Facts_about_the_blood_supply.add("Blood cannot be manufactured - it can only come from geneous donor.");
        Facts_about_the_blood_supply.add("Type O-negative blood(red cells) can be transfused to patients of all blood types. It is always in great demand and often in short supply.");
        Facts_about_the_blood_supply.add("Type AB+positive plasma can be transfused to patients of all other blood types. AB  plasma is also usually in short supply.");


        List<String> donation_process = new ArrayList<String>();
    //    donation_process.add("United States");

        List<String> components = new ArrayList<String>();
     //   components.add("India");


        List<String> about_donors = new ArrayList<String>();
      //  about_donors.add("Brazil");


        List<String> example = new ArrayList<String>();
    //    example.add("United States");

        List<String> blood_types = new ArrayList<String>();
    //    blood_types.add("India");


        List<String> who_donate = new ArrayList<String>();
     //   who_donate.add("Brazil");


        List<String> who_cannot_donate = new ArrayList<String>();
   //     who_cannot_donate.add("United States");


        expandableListDetail.put("Facts about blood needs", Facts_about_blood_needs);
        expandableListDetail.put("Facts about the blood supply", Facts_about_the_blood_supply);
        expandableListDetail.put("Facts about the blood donation process", donation_process);
        expandableListDetail.put("Facts about blood and its components", components);
        expandableListDetail.put("Facts about donors", about_donors);
        expandableListDetail.put("Example of blood use", example);
        expandableListDetail.put("Frequency of blood types", blood_types);
        expandableListDetail.put("Who can donate", who_donate);
        expandableListDetail.put("Who can not donate", who_cannot_donate);
        return expandableListDetail;
    }
}
