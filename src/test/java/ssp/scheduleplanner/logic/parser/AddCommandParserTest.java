package ssp.scheduleplanner.logic.parser;

import static ssp.scheduleplanner.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.DATE_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.DATE_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static ssp.scheduleplanner.logic.parser.CommandParserTestUtil.assertParseFailure;
import static ssp.scheduleplanner.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static ssp.scheduleplanner.testutil.TypicalTasks.AMY;
import static ssp.scheduleplanner.testutil.TypicalTasks.BOB;

import org.junit.Test;

import ssp.scheduleplanner.logic.commands.AddCommand;
import ssp.scheduleplanner.model.tag.Tag;
import ssp.scheduleplanner.model.task.Date;
import ssp.scheduleplanner.model.task.Name;
import ssp.scheduleplanner.model.task.Priority;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.model.task.Venue;
import ssp.scheduleplanner.testutil.TaskBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Task expectedTask = new TaskBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple dates - last date accepted
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_AMY + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple tags - all accepted
        Task expectedTaskMultipleTags = new TaskBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, new AddCommand(expectedTaskMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Task expectedTask = new TaskBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + DATE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedTask));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing date prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_DATE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_DATE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_DATE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Date.MESSAGE_DATE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Priority.MESSAGE_PRIORITY_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Venue.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + DATE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
