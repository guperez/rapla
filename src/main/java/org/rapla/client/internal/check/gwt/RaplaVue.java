package org.rapla.client.internal.check.gwt;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(name = "raplaVue", isNative = true, namespace = JsPackage.GLOBAL)
public class RaplaVue {

	@JsMethod(name = "$emit")
	public static native void emit(String eventname, Object params);
	
}
