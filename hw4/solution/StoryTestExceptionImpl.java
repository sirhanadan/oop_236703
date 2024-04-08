package solution;

import provided.StoryTestException;

public class StoryTestExceptionImpl extends StoryTestException
{
    private String storySentence;
    private String expectedV;
    private String resultV;
    private int failCnt;
    StoryTestExceptionImpl(String sentence, String expected, String result)
    {
        this.storySentence = sentence;
        this.expectedV = expected;
        this.resultV = result;
        this.failCnt = 0;
    }

    public void setFails(int num) {
    	this.failCnt = num;
    }


    @Override
    public String getSentance()
    {
        return this.storySentence;
    }

    @Override
    public String getStoryExpected()
    {
        return this.expectedV;
    }

    @Override
    public String getTestResult()
    {
        return this.resultV;
    }

    @Override
    public int getNumFail()
    {
        return this.failCnt;
    }


}
