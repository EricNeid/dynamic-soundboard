package org.neidhardt.dynamicsoundboard.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table SOUND_SHEET.
 */
public class SoundSheet {

    private Long id;
    /** Not-null value. */
    private String fragmentTag;
    /** Not-null value. */
    private String label;
    private boolean isSelected;

    // KEEP FIELDS - put your custom fields here
    private boolean isSelectedForDeletion = false;
    // KEEP FIELDS END

    public SoundSheet() {
    }

    public SoundSheet(Long id) {
        this.id = id;
    }

    public SoundSheet(Long id, String fragmentTag, String label, boolean isSelected) {
        this.id = id;
        this.fragmentTag = fragmentTag;
        this.label = label;
        this.isSelected = isSelected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getFragmentTag() {
        return fragmentTag;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    /** Not-null value. */
    public String getLabel() {
        return label;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLabel(String label) {
        this.label = label;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundSheet that = (SoundSheet) o;

        return fragmentTag.equals(that.fragmentTag);

    }

    public boolean isSelectedForDeletion() {
        return isSelectedForDeletion;
    }

    public void setIsSelectedForDeletion(boolean isSelectedForDeletion) {
        this.isSelectedForDeletion = isSelectedForDeletion;
    }

    @Override
    public int hashCode() {
        return fragmentTag.hashCode();
    }

    @Override
    public String toString() {
        return "SoundSheet{" +
                "id=" + id +
                ", fragmentTag='" + fragmentTag + '\'' +
                ", label='" + label + '\'' +
                ", isSelected=" + isSelected +
                ", isSelectedForDeletion=" + isSelectedForDeletion +
                '}';
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
