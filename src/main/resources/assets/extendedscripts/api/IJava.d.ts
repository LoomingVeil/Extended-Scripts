import { IObject } from "./IObject"

declare namespace Java {
    /**
     * Gets the java class from a string.
     * Ex: "java.lang.System" returns the class that allows you print to console
     * Java.type("java.lang.System").out.println("Hello, World")
     */
    function type<T>(className: string): any;
    function extend(baseType: any, impl: any): any;
    function isJavaFunction(obj: IObject)
    function isScriptObject(obj: IObject)
    function isScriptFunction(obj: IObject)
}
