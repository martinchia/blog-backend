package com.cloud.blog.dataObject;

public class Videos {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column videos.id
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column videos.src
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    private String src;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column videos.discription
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    private String discription;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column videos.source
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    private Integer source;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column videos.id
     *
     * @return the value of videos.id
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column videos.id
     *
     * @param id the value for videos.id
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column videos.src
     *
     * @return the value of videos.src
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public String getSrc() {
        return src;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column videos.src
     *
     * @param src the value for videos.src
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public void setSrc(String src) {
        this.src = src == null ? null : src.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column videos.discription
     *
     * @return the value of videos.discription
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column videos.discription
     *
     * @param discription the value for videos.discription
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public void setDiscription(String discription) {
        this.discription = discription == null ? null : discription.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column videos.source
     *
     * @return the value of videos.source
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public Integer getSource() {
        return source;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column videos.source
     *
     * @param source the value for videos.source
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    public void setSource(Integer source) {
        this.source = source;
    }
}