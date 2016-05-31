package io.statful.client.domain.api;

import io.statful.client.domain.api.Tags;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class TagsTest {

    @Test
    public void shouldGetFromTypeAndValue() throws Exception {
        Tags tags = Tags.from("unit", "ms");

        assertEquals("ms", tags.getTagValue("unit"));
    }

    @Test
    public void shouldGetFromOtherTag() throws Exception {
        Tags tags = Tags.from(Tags.from("unit", "ms"));

        assertEquals("ms", tags.getTagValue("unit"));
    }

    @Test
    public void shouldGetFromTagsArray() throws Exception {
        Tags tags = Tags.from(new String[] {"unit", "ms", "cluster", "production"});

        assertEquals(2, tags.getTags().size());
        assertEquals("ms", tags.getTagValue("unit"));
        assertEquals("production", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldNotGetFromTagsArrayWhenIsNotEven() throws Exception {
        Tags tags = Tags.from(new String[] {"unit"});

        assertNull(tags);
    }

    @Test
    public void shouldNotGetFromTagsArrayWhenIsEmpty() throws Exception {
        Tags tags = Tags.from(new String[] {});

        assertNull(tags);
    }

    @Test
    public void shouldPutTag() throws Exception {
        Tags tags = new Tags();
        tags.putTag("unit", "ms");

        assertEquals("ms", tags.getTagValue("unit"));
    }

    @Test
    public void shouldGetTags() throws Exception {
        Tags tags = Tags.from("unit", "ms");

        assertEquals("ms", tags.getTags().get("unit"));
    }

    @Test
    public void shouldMergeTags() throws Exception {
        Tags tags = Tags.from("unit", "ms");

        tags.merge(Tags.from("cluster", "production"));

        assertEquals("ms", tags.getTags().get("unit"));
        assertEquals("production", tags.getTags().get("cluster"));
    }

    @Test
    public void shouldNotMergeTagsIfArgumentIsNull() throws Exception {
        Tags tags = Tags.from("unit", "ms");

        tags.merge(null);

        assertEquals("ms", tags.getTags().get("unit"));
        assertEquals(1, tags.getTags().size());
    }

    @Test
    public void shouldCheckIfTagTypeIsEmpty() throws Exception {
        assertTrue(Tags.isEmptyOrNull("", "ms"));
    }

    @Test
    public void shouldCheckIfTagTypeIsNull() throws Exception {
        assertTrue(Tags.isEmptyOrNull(null, "ms"));
    }

    @Test
    public void shouldCheckIfTagValueIsEmpty() throws Exception {
        assertTrue(Tags.isEmptyOrNull("unit", ""));
    }

    @Test
    public void shouldCheckIfTagValueIsNull() throws Exception {
        assertTrue(Tags.isEmptyOrNull("unit", null));
    }

    @Test
    public void shouldCheckIfTypeAndValueAreEmpty() throws Exception {
        assertTrue(Tags.isEmptyOrNull("", ""));
    }

    @Test
    public void shouldCheckIfTypeAndValueAreNull() throws Exception {
        assertTrue(Tags.isEmptyOrNull(null, null));
    }

    @Test
    public void shouldCheckIfTagIsNotEmpty() throws Exception {
        assertFalse(Tags.isEmptyOrNull("unit", "ms"));
    }
}