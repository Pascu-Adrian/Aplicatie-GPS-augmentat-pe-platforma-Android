/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gps.augmentat.rutare.astarrenew;




import gps.augmentat.rutare.Concret.CalculDistanta;
import gps.augmentat.rutare.Concret.ComparatorTest;
import gps.augmentat.rutare.Concret.Incarcare;
import gps.augmentat.rutare.abstracte.ICalculDistantaNod;
import gps.augmentat.rutare.object.NodObiect;
import gps.augmentat.rutare.object.Ruta;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Vector;




public class AStarRenew {

 
    
    /*public static void main(String[] args) {
        // TODO code application logic here
        
//        for(NodObiect n:noade)
//        {
//            System.out.println(n.getId());
//            for(NodObiect nv:n.getListaVecini())
//            {
//                
//                System.out.println("--"+nv.getId());
//            }
//        }
    	Incarcare i=new Incarcare();
        Vector<NodObiect> noade=i.incarcaDinXml("noduri.xml");
        NodObiect nodBegin=null;
        NodObiect nodEnd=null;
        for(NodObiect n:noade)
        {
            if(n.getId().equals("64")) nodBegin=n;
            if(n.getId().equals("20")) nodEnd=n;
        }
        ICalculDistantaNod del=new CalculDistanta();
        Ruta<NodObiect> ruta=FindMinPath(nodBegin, nodEnd, del);
        NodObiect nod=ruta.getUltimulPas();
        while(ruta!=null)
        {
          nod=ruta.getUltimulPas();
          System.out.println(nod.getId());
          ruta=ruta.UltimiiPasi();
        }
    }*/
    public static Ruta<NodObiect> FindMinPath(NodObiect nodInceput,NodObiect nodSfarsit, ICalculDistantaNod distanta)
    {
      
        HashSet<NodObiect> vizitat = new HashSet<NodObiect>();
        PriorityQueue<Ruta<NodObiect>> queue=new PriorityQueue<Ruta<NodObiect>>(1, new ComparatorTest());
           
            Ruta<NodObiect> caleBegin=new Ruta<NodObiect>(nodInceput);
            queue.add(caleBegin);
            while (!queue.isEmpty())
            {
                Ruta<NodObiect> cale = queue.poll();
                if (vizitat.contains(cale.getUltimulPas())) continue;
                if (cale.getUltimulPas().equals(nodSfarsit)) return cale;
                vizitat.add(cale.getUltimulPas());
                for(NodObiect n:cale.getUltimulPas().getListaVecini())
                {
                    double d=distanta.distantaLa(cale.getUltimulPas(),n);
                    double e=distanta.distantaLa(n, nodSfarsit);
                    Ruta<NodObiect> newCale=cale.AdaugaPas(n, d+e);
                    queue.add(newCale);
                }
            }

            return null;
    }

}
