// IMainInterface.aidl
package net.chris.demo.guardian;

// Declare any non-default types here with import statements
import java.util.Map;

interface IMainInterface {

   boolean fetchContent(in Map filter);

   boolean fetchSection();

}
