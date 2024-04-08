package solution;

import org.junit.ComparisonFailure;
import provided.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import solution.StoryTestExceptionImpl;

public class StoryTesterImpl implements StoryTester {

    private Object objectBackup;

    String firstFailedSentence;
    String expected;
    String result;
    int numFails;

/**************************************************************************************************/
    /** Creates and returns a new instance of testClass **/
    private static Object createTestInstance(Class<?> testClass) throws Exception {
   	
       	Class<?> outerClass = testClass.getEnclosingClass();
       	
       	if(outerClass == null || (Modifier.isStatic(testClass.getModifiers()))) {
       		//it means that this is an outer class and there is nothing enclosing it
       		//or that it is a static object 
       		// TODO: Try constructing a new instance using the default constructor of testClass
       		Constructor<?> defCtr = testClass.getDeclaredConstructor(); //get the constructor
       		defCtr.setAccessible(true); //set it to public
       		return (defCtr.newInstance()); //return the new instance
       		
       		
       	}else {
        	//it means that this is an inner class and there is something enclosing it
        	// TODO: Inner classes case; Need to first create an instance of the enclosing class
        	Object outerInstance = createTestInstance(outerClass);
        	
        	Constructor<?> ctor = testClass.getDeclaredConstructor(outerClass); //get the constructor
        	ctor.setAccessible(true); //set it to public
        	return (ctor.newInstance(outerInstance)); //return the new instance
        
        }
        	
    }
/**************************************************************************************************/
    /** Returns true if c has a copy constructor, or false if it doesn't **/
    private boolean copyConstructorExists(Class<?> c){
        try {
            c.getDeclaredConstructor(c);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
    
    /** Returns the copy constructor if cls has a copy constructor, or null if it doesn't **/
    private Constructor<?> getCopyCtr(Class<?> cls){
    	try {
	    	if(this.copyConstructorExists(cls)) {
	    		return cls.getDeclaredConstructor(cls);
	    	}else{
	    		return null;
	    	}
    	}
    	catch(Exception e) {
    		return null;
    	}
    	
    }
/**************************************************************************************************/

    /** Assigns into objectBackup a backup of obj.
    /** See homework's pdf for more details on backing up and restoring **/
    private void backUpInstance(Object obj) throws Exception {
        Object res = createTestInstance(obj.getClass());
        Field[] fieldsArr = obj.getClass().getDeclaredFields();
        for(Field field : fieldsArr){
            field.setAccessible(true);
            Object fieldObject = field.get(obj); //the value of the field in a certain object
            if (fieldObject == null) {
                field.set(res, null);
                continue;
            }
            Class<?> fieldClass = fieldObject.getClass();
            
            Object newVal;

            if(fieldObject instanceof Cloneable){
                // TODO: Case1 - Object in field is cloneable
            	Method cm; //cm = clone method
            	try {
            		//look for clone in the methods
            		cm = fieldClass.getMethod("clone"); //look for a public one in the inheritance tree
            	}catch(Exception e) {
            		cm = fieldClass.getDeclaredMethod("clone"); //look for any one in the current class      		
            	}
            	cm.setAccessible(true);
            	newVal = cm.invoke(fieldObject);
            }
            else if(copyConstructorExists(fieldClass)){
                // TODO: Case2 - Object in field is not cloneable but copy constructor exists
            	Constructor <?> cpyCtr = this.getCopyCtr(fieldClass);
            	cpyCtr.setAccessible(true);
            	newVal = cpyCtr.newInstance(fieldObject);
            }
            else{
                // TODO: Case3 - Object in field is not cloneable and copy constructor does not exist
            	newVal = fieldObject;
            
            }
            field.set(res, newVal);
            
            
        }
        this.objectBackup = res;
        
    }
    
/**************************************************************************************************/

    /** Assigns into obj's fields the values in objectBackup fields.
    /** See homework's pdf for more details on backing up and restoring **/
    private void restoreInstance(Object res) throws Exception{
        Field[] classFields = res.getClass().getDeclaredFields();
        for(Field field : classFields) {
            // TODO: Complete.
        	//copied from the backup above but switched places:
        	
        	field.setAccessible(true);
            Object fieldObject = field.get(objectBackup); //the value of the field in a certain object
            if (fieldObject == null) {
                field.set(res, null);
                continue;
            }
            Class<?> fieldClass = fieldObject.getClass();
            
            Object newVal;

            if(fieldObject instanceof Cloneable){
                // TODO: Case1 - Object in field is cloneable
            	Method cm; //cm = clone method
            	try {
            		//lok for clone in the methods
            		cm = fieldClass.getMethod("clone"); //look for a public one in the inheritance tree
            	}catch(Exception e) {
            		cm = fieldClass.getDeclaredMethod("clone"); //look for any one in the current class      		
            	}
            	cm.setAccessible(true);
            	newVal = cm.invoke(fieldObject);
            }
            else if(copyConstructorExists(fieldClass)){
                // TODO: Case2 - Object in field is not cloneable but copy constructor exists
            	Constructor <?> cpyCtr = this.getCopyCtr(fieldClass);
            	cpyCtr.setAccessible(true);
            	newVal = cpyCtr.newInstance(fieldObject);
            }
            else{
                // TODO: Case3 - Object in field is not cloneable and copy constructor does not exist
            	newVal = fieldObject;
            
            }
            field.set(res, newVal);
        	
        	
        }
    }

/**************************************************************************************************/

    /** Returns the matching annotation class according to annotationName (Given, When or Then) **/
    /*
    private static Class<? extends Annotation> GetAnnotationClass(String annotationName){
        switch (annotationName) {
            // TODO: Return matching annotation class
        case "Given": 
        	return Given.class;
		case "When":
        	return When.class;
		case "Then":
        	return Then.class;
        }
    }
    
    */
    
    
    
/**************************************************************************************************/

    
    /** create a class to parse and handle the lines of the story */
    
    public static class LineReqs{
    	
    	private final String statemnetLine; /**given a dog of age 6*/
        private final String statementAnno; /**given*/
        private final String statementMid; /**a dog of age */
        private final String statementVal; /**6*/
        
        LineReqs(String l){
        	this.statemnetLine = l; 
        	String[] words = l.split(" ");
        	//the main word is the first one:
        	this.statementAnno = words[0];
        	this.statementVal = words[words.length - 1 ];
        	this.statementMid = l.substring(l.indexOf(" ") + 1, l.lastIndexOf(" "));
        }
        String getStatement() {
        	return this.statemnetLine;
        }
        String getStatementMid() {
        	return this.statementMid;
        }
        String getStatementAnno() {
        	return this.statementAnno;
        }
        String getStatementValue() {
        	return this.statementVal;
        }
        
        
        public void throwCorrectException() throws WordNotFoundException {
        	if(this.getStatementAnno().equals("Given")) {
        		throw new GivenNotFoundException();
        	}
        	else if(this.getStatementAnno().equals("When")) {
        		throw new WhenNotFoundException();
        	}
        	else if(this.getStatementAnno().equals("Then")) {
        		throw new ThenNotFoundException();
        	}
        	
        	throw new WordNotFoundException();
        	
        }
        
        public boolean isMethodWithSameAnnotation(Method meth) {
        	String a = null; //the annotation value
        	if(this.getStatementAnno().equals("Given")) {
        		Given g =  meth.getAnnotation(Given.class);
        		if(g == null) {
        			a = null;
        		}else {
        			a = g.value();
        		}
        	} else if(this.getStatementAnno().equals("When")) {
        		When g =  meth.getAnnotation(When.class);
        		if(g == null) {
        			a = null;
        		}else {
        			a = g.value();
        		}
        	} else if(this.getStatementAnno().equals("Then")) {
        		Then g =  meth.getAnnotation(Then.class);
        		if(g == null) {
        			a = null;
        		}else {
        			a = g.value();
        		}
        	}
        	//if the method doesn't have the same annotation we're looking for
        	if (a == null ) {
        		return false;
        	}
        	
        	// remove the last word aka the value and compare to mContent
        	return ((a.substring(0, a.lastIndexOf(" "))).equals(this.getStatementMid()));
        	
        	
        }
    	
            
    }
    
    
/**************************************************************************************************/
    /** recursively search the inheritance tree for a method with a specific annotation */
    public static Method getMethodWithSameAnnotation(Class<?> testClass, LineReqs testReqs) {
    	
    	//search the current class for a method with the specified annotation
    	for(Method meth : testClass.getDeclaredMethods()) {
    		if(testReqs.isMethodWithSameAnnotation(meth)) {
    			return meth;
    		}
    	}
    	//if none were found, search the super class until there are none left
    	if(testClass != Object.class) {
    		return getMethodWithSameAnnotation(testClass.getSuperclass(), testReqs);
    	}
    	//no superclass had the correct annotation, return null
    	return null;
    	       	
    	
    }
/**************************************************************************************************/
    /** invoke the given method - make sure the parameter is the appropriate type*/
    public static void invokeMethodTest(Object testedObject, LineReqs testReqs, Method testedMeth) throws Exception
    {
    	//change the the tested method access to public
        testedMeth.setAccessible(true);
        //get the value it should return
        String arg = testReqs.getStatementValue();
        //is the return value string or integer (for the sake of conversion)
        if(testedMeth.getParameterTypes()[0] == Integer.class || testedMeth.getParameterTypes()[0] == int.class)
            testedMeth.invoke(testedObject, Integer.parseInt(testReqs.getStatementValue()));
        else
            testedMeth.invoke(testedObject, arg);
    }
    
/**************************************************************************************************/

    
    

    /** search nested classes for the given annotation  */
    private Class<?> findClassWithGivenStatement_nestedTest(LineReqs testReq, Class<?> testClass){
    	
    	if(getMethodWithSameAnnotation(testClass, testReq) != null) { //base case
    		return testClass;
    	}
    	
    	for(Class<?> nestedClass : testClass.getDeclaredClasses()) { //recursive call
    		
    		if(nestedClass.isInterface()) { //even if an interface has the given annotation, it does not have an implementation
    			continue;
    		}
    		//search the nested classes for the annotation
    		if(getMethodWithSameAnnotation(nestedClass, testReq) != null) {
    			return nestedClass;
    		}else if(testClass != Object.class) { //when we reach Object class we stop
    			//search each of the nested classes' nested classes for the annotation
    			Class<?> res = findClassWithGivenStatement_nestedTest(testReq, nestedClass);
    			if(res != null) {
    				return res;
    			}
    		}

    		
    	}
    	return null;
    	
    	
    }
    
    
/**************************************************************************************************/

    
    /*
	* private Object objectBackup;
    * String firstFailedSentence;
    * String expected;
    * String result;
    * int numFails; 
    * */
    
    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        if((story == null) || testClass == null) throw new IllegalArgumentException();

        this.numFails = 0;
        Object testInstance = createTestInstance(testClass);
        
        
        /* added initializations */
        this.objectBackup = null;
        this.firstFailedSentence = "";
        this.expected = "";
        this.result = "";
        
        StoryTestExceptionImpl exp = null; 
                boolean firstWhen = true;
        
        //backup the current object:
        this.backUpInstance(testInstance);
        for(String sentence : story.split("\n")) {
            //boolean methodFound = false;
            /**
            String[] words = sentence.split(" ", 2);

            String annotationName = words[0];
            Class<? extends Annotation> annotationClass = GetAnnotationClass(annotationName);

            String sentenceSub = words[1].substring(0, words[1].lastIndexOf(' ')); // Sentence without the parameter and annotation
            String parameter = sentence.substring(sentence.lastIndexOf(' ') + 1);
            
            // TODO: Complete.
             * */
            LineReqs testReq = new LineReqs(sentence);
            Method meth = getMethodWithSameAnnotation(testClass, testReq);
            if(meth == null) {
            	testReq.throwCorrectException();
            }
            
            if(testReq.getStatementAnno().equals("When") && firstWhen) {

            	this.backUpInstance(testInstance);
            	firstWhen = false;
            	
            }
            else {
            	if(!testReq.getStatementAnno().equals("When")) {
            		firstWhen = true;
            	}
            }
                        
            try {
            	
            	invokeMethodTest(testInstance, testReq,meth);
            }
            catch(InvocationTargetException e) {
            	Throwable target = e.getTargetException();
            	if(!(target instanceof ComparisonFailure)) {
            		throw e;
            	}
            	else {
            		ComparisonFailure cf = (ComparisonFailure) target;
            		            		
            		if(exp == null) {
            			exp = new StoryTestExceptionImpl(testReq.getStatement(), cf.getExpected(), cf.getActual());
            		}
            		
//            		System.out.println(cf.getExpected());
//            		System.out.println(cf.getActual());

            		this.restoreInstance(testInstance);
            		
            		
            	}
            	if(testReq.getStatementAnno().equals("Then")) {

            		this.numFails++;
            		
            	}
            }
  
             
        }
        
     // TODO: Throw StoryTestExceptionImpl if the story failed.
        if(exp != null) {
        	exp.setFails(numFails);
        	throw exp;
        }

        
    }

/**************************************************************************************************/

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        // TODO: Complete.
    	if((story == null) || testClass == null) throw new IllegalArgumentException();
    	
    	String fstSentence = story.split("\n")[0];
    	LineReqs testReq = new LineReqs(fstSentence);
    	Class<?> newTestClass = findClassWithGivenStatement_nestedTest(testReq, testClass);
    	testOnInheritanceTree(story, newTestClass);
    }
}
