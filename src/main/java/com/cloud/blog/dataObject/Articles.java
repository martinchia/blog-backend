package com.cloud.blog.dataObject;

public class Articles {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column articles.id
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column articles.title
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column articles.context
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    private String context;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column articles.id
     *
     * @return the value of articles.id
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column articles.id
     *
     * @param id the value for articles.id
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column articles.title
     *
     * @return the value of articles.title
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column articles.title
     *
     * @param title the value for articles.title
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column articles.context
     *
     * @return the value of articles.context
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    public String getContext() {
        return context;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column articles.context
     *
     * @param context the value for articles.context
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }
}