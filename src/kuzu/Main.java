package kuzu;
import com.kuzudb.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private final static String db_path = "/Users/owenlipchitz/Owen/programming/databaseV1";
    //private final static String db_path = "/home/2020/csacre/small";


    private final static KuzuDatabase db = new KuzuDatabase(db_path);
    private final static KuzuConnection conn = new KuzuConnection(db);

    public static void main(String[] args) throws KuzuObjectRefDestroyedException {
        if (args.length < 1 ) {
            System.out.println("Missing argument: create, cli, test");
            System.exit(0);
        }
        String arg = args[0];
        switch (arg) {
            case "create":
                createDB();
                break;
            case "cli":
                cmdLine();
                break;
            case "test":
                test();
                break;
            default:
                System.out.println("Invalid mode");
        }
    }

    public static void createDB() throws KuzuObjectRefDestroyedException {
        // Create tables


//        conn.query("CREATE NODE TABLE Company (id ID, name STRING, url STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Forum (id ID, title STRING, creationDate DATETIME, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Message (id ID, browserUsed STRING, creationDate DATETIME, " +
//                "locationIP STRING, content STRING, length INT32, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Organisation (id ID, name STRING, url STRING, PRIMARY KEY(id))");



//        conn.query("CREATE NODE TABLE Person (id INT64, firstName STRING, lastName STRING, gender STRING, " +
//                "birthday INT64, creationDate INT64, locationIP STRING, browserUsed STRING," +
//                "language SET<STRING>, email SET<STRING>, PRIMARY KEY(id))");

//FINISHED QUERIES----------------------------------------------------------------------------------------------------------------------------------------------
//        Entity Table creation queries
//        conn.query("CREATE NODE TABLE Person(id INT64, firstName STRING, lastName STRING, gender STRING, birthday INT64, creationDate INT64, locationIP STRING, browserUsed STRING,language STRING, email STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Place(id INT64, name STRING, url STRING, type String, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Comment (id INT64, creationDate INT64, locationIP STRING,browserUsed STRING, content STRING, length INT64, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Tag(id INT64, name STRING, url STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Forum(id INT64, title STRING, creationDate INT64, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Organisation (id INT64, type STRING, name STRING, url STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Post(id INT64, imageFile STRING, creationDate INT64, locationIP STRING, browserUsed STRING, language STRING, content STRING, length INT64, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE TagClass (id INT64, name STRING, url STRING, PRIMARY KEY(id))");
//        Relationship Table creation queries
//        conn.query("CREATE REL TABLE COMMENT_HAS_CREATOR_PERSON(FROM Comment to Person)");
//        conn.query("CREATE REL TABLE COMMENT_HAS_TAG_TAG(FROM Comment to Tag)");
//        conn.query("CREATE REL TABLE COMMENT_IS_LOCATED_IN_PLACE(FROM Comment to Place)");
//        conn.query("CREATE REL TABLE COMMENT_REPLY_OF_COMMENT(FROM Comment to Comment)");
//        conn.query("CREATE REL TABLE COMMENT_REPLY_OF_POST(FROM Comment to Post)");

//        conn.query("CREATE REL TABLE FORUM_CONTAINER_OF_POST(FROM Forum to Post)");
//        conn.query("CREATE REL TABLE FORUM_HAS_MEMBER(FROM Forum to Person, joinDate INT64)");
//        conn.query("CREATE REL TABLE FORUM_HAS_MODERATOR(FROM Forum to Person)");
//        conn.query("CREATE REL TABLE FORUM_HAS_TAG_TAG(FROM FORUM to Tag)");

//        conn.query("CREATE REL TABLE ORGANISATION_IS_LOCATED_IN_PLACE(FROM Organisation to Place)");

//         conn.query("CREATE REL TABLE PERSON_HAS_INTEREST_TAG(FROM Person to Tag)");
//        conn.query("CREATE REL TABLE PERSON_IS_LOCATED_IN_PLACE(FROM Person to Place)");
//        conn.query("CREATE REL TABLE PERSON_KNOWS_PERSON(FROM Person to Person, creationDate INT64)");
//        conn.query("CREATE REL TABLE PERSON_LIKES_COMMENT(FROM Person to Comment, creationDate INT64)");
//        conn.query("CREATE REL TABLE PERSON_LIKES_POST(FROM Person to Post, creationDate INT64)");
//        conn.query("CREATE REL TABLE PERSON_STUDY_AT_ORGANISATION(FROM Person to Organisation, classYear INT64)");
//        conn.query("CREATE REL TABLE PERSON_WORK_AT_ORGANISATION(FROM Person to Organisation, workFrom INT64)");

//        conn.query("CREATE REL TABLE PLACE_IS_PART_OF(FROM Place to Place)");

//        conn.query("CREATE REL TABLE POST_HAS_CREATOR_PERSON(FROM Post to Person)");
//        conn.query("CREATE REL TABLE POST_HAS_TAG_TAG(FROM Post to Tag)");
//        conn.query("CREATE REL TABLE POST_IS_LOCATED_IN_PLACE(FROM Post to Place)");

//         conn.query("CREATE REL TABLE HAS_TYPE(FROM Tag to TagClass)");
//
//        conn.query("CREATE REL TABLE TAGCLASS_IS_SUBCLASS_OF(FROM TagClass to TagClass)");

        //----------------------------------------------------------------------------------------------------------------------------------------------------
//Create Relational table for the Tag has type tagclass relation having a Tag id and a Tagclass id















//        conn.query("CREATE NODE TABLE Place (id ID, name STRING, url STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Post (id ID, language STRING, imageFile STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE Tag (id ID, name STRING, url STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE TagClass (id ID, name STRING, url STRING, PRIMARY KEY(id))");
//        conn.query("CREATE NODE TABLE University (id ID, name STRING, url STRING, PRIMARY KEY(id))");
//
//
//        conn.query("CREATE REL TABLE CONTAINER_OF(FROM Forum to Post)");
//        conn.query("CREATE REL TABLE HAS_CREATOR(FROM Message to Person)");
//        conn.query("CREATE REL TABLE HAS_INTEREST(FROM Person to Tag)");
//        conn.query("CREATE REL TABLE HAS_MEMBER(FROM Forum to Person)");
//        conn.query("CREATE REL TABLE HAS_MODERATOR(FROM Forum to Person)");
//        conn.query("CREATE REL TABLE HAS_TAG_MESSAGE(FROM Message to Tag)");
//        conn.query("CREATE REL TABLE HAS_TAG_FORUM(FROM Forum to Tag)");
//        conn.query("CREATE REL TABLE HAS_TYPE(FROM Tag to TagClass)");
//        conn.query("CREATE REL TABLE IS_LOCATED_IN_COMPANY(FROM Company to Country)");
//        conn.query("CREATE REL TABLE IS_LOCATED_IN_MESSAGE(FROM Message to Country)");
//        conn.query("CREATE REL TABLE IS_LOCATED_IN_PERSON(FROM Person to City)");
//        conn.query("CREATE REL TABLE IS_LOCATED_IN_UNIVERSITY(FROM University to City)");
//        conn.query("CREATE REL TABLE IS_PART_OF_CITY(FROM City to Country)");
//        conn.query("CREATE REL TABLE IS_PART_OF_COUNTRY(FROM Country to Continent)");
//        conn.query("CREATE REL TABLE IS_SUBCLASS_OF_TAG_CLASS(FROM TagClass to TagClass)");
//        conn.query("CREATE REL TABLE KNOWS(FROM Person to Person)");
//        conn.query("CREATE REL TABLE LIKES(FROM Person to Message)");
//        conn.query("CREATE REL TABLE REPLY_OF(FROM Comment to Message)");
//        conn.query("CREATE REL TABLE STUDY_AT(FROM Person to University)");
//        conn.query("CREATE REL TABLE WORK_AT(FROM Person to Company)");



    /*
        conn.query("CREATE NODE TABLE Post(postId INT64, title STRING, body STRING, " +
                "score INT64, views INT64, comments INT64, PRIMARY KEY(postId))");
        conn.query("CREATE NODE TABLE Tag(tagId STRING, PRIMARY KEY(tagId))");
        conn.query("CREATE NODE TABLE User(userId INT64, displayname STRING, reputation INT64," +
                "aboutme STRING, websiteurl STRING, location STRING, profileimageurl STRING, " +
                "views INT64, upvotes INT64, downvotes INT64, PRIMARY KEY (userId))");

        conn.query("CREATE REL TABLE PARENT_OF(FROM Post to Post)");
        conn.query("CREATE REL TABLE HAS_TAG(FROM Post to Tag)");
        conn.query("CREATE REL TABLE POSTED(FROM User to Post)");
*/
        // data clean up e.g. make sure column 1 of users_posts_rel ($1 in seen) is in column 1 of new users (seen[$1])
        //awk -F',' 'NR==FNR{seen[$1]; next} ($1 in seen)'  new_users3.csv users_posts_rel.csv > new_users_posts_rel2.csv
        // ignore non-utf chars: iconv -f UTF-8 -t UTF-8//IGNORE new_posts.csv > new_posts2.csv
        // replace null by isNull: sed "s/null/isNull/g" tags_posts_rel.csv > new_tags_posts_rel.csv



        // Load data. "~/Owen/programming/"
        /*
    bin/neo4j-admin database import full --skip-bad-relationships --skip-duplicate-nodes --bad-tolerance 1000000000\
    --id-type=INTEGER \
    --nodes=Place="import/place_0_0.csv" \
    --nodes=Organisation="import/organisation_0_0.csv" \
    --nodes=TagClass="import/tagclass_0_0.csv" \
    --nodes=Tag="import/tag_0_0.csv" \
    --nodes=Comment:Message="import/comment_0_0.csv" \
    --nodes=Forum="import/forum_0_0.csv" \
    --nodes=Person="import/person_0_0.csv" \
    --nodes=Post:Message="import/post_0_0.csv" \
    --relationships=IS_PART_OF="import/place_isPartOf_place_0_0.csv" \
    --relationships=IS_SUBCLASS_OF="import/tagclass_isSubclassOf_tagclass_0_0.csv" \
    --relationships=IS_LOCATED_IN="import/organisation_isLocatedIn_place_0_0.csv" \
    --relationships=HAS_TYPE="import/tag_hasType_tagclass_0_0.csv" \
    --relationships=HAS_CREATOR="import/comment_hasCreator_person_0_0.csv" \
    --relationships=IS_LOCATED_IN="import/comment_isLocatedIn_place_0_0.csv" \
    --relationships=REPLY_OF="import/comment_replyOf_comment_0_0.csv" \
    --relationships=REPLY_OF="import/comment_replyOf_post_0_0.csv" \
    --relationships=CONTAINER_OF="import/forum_containerOf_post_0_0.csv" \
    --relationships=HAS_MEMBER="import/forum_hasMember_person_0_0.csv" \
    --relationships=HAS_MODERATOR="import/forum_hasModerator_person_0_0.csv" \
    --relationships=HAS_TAG="import/forum_hasTag_tag_0_0.csv" \
    --relationships=HAS_INTEREST="import/person_hasInterest_tag_0_0.csv" \
    --relationships=IS_LOCATED_IN="import/person_isLocatedIn_place_0_0.csv" \
    --relationships=KNOWS="import/person_knows_person_0_0.csv" \
    --relationships=LIKES="import/person_likes_comment_0_0.csv" \
    --relationships=LIKES="import/person_likes_post_0_0.csv" \
    --relationships=HAS_CREATOR="import/post_hasCreator_person_0_0.csv" \
    --relationships=HAS_TAG="import/comment_hasTag_tag_0_0.csv" \
    --relationships=HAS_TAG="import/post_hasTag_tag_0_0.csv" \
    --relationships=IS_LOCATED_IN="import/post_isLocatedIn_place_0_0.csv" \
    --relationships=STUDY_AT="import/person_studyAt_organisation_0_0.csv" \
    --relationships=WORK_AT="import/person_workAt_organisation_0_0.csv" \
    --delimiter '|'
     */


        // KuzuQueryResult r4 = conn.query("COPY PARENT_OF FROM './dataset/small/new_posts_rel2.csv' (HEADER=true)");
        //        System.out.println("Parent: " + r4.toString());

//FINISHED QUERIES-------------------------------
//        KuzuQueryResult r1 = conn.query("COPY Person FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("Person: " + r1.toString());
//        KuzuQueryResult r2 = conn.query("COPY Place FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/place_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("Place: " + r2.toString());
//        KuzuQueryResult r3 = conn.query("COPY Comment FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/comment_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("Comment: " + r3.toString());
//        KuzuQueryResult r4 = conn.query("COPY Tag FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/tag_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("Tag: " + r4.toString());
//        KuzuQueryResult r5 = conn.query("COPY Forum FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/forum_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("Forum: " + r5.toString());
//        KuzuQueryResult r6 = conn.query("COPY Organisation FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/organisation_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("Organisation: " + r6.toString());
//        KuzuQueryResult r7 = conn.query("COPY Post FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/post_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("Post: " + r7.toString());
//        KuzuQueryResult r8 = conn.query("COPY TagClass FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/tagclass_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("TagClass: " + r8.toString());


//         KuzuQueryResult r9 = conn.query("COPY COMMENT_HAS_CREATOR_PERSON FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/comment_hasCreator_person_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("COMMENT_HAS_CREATOR_PERSON: " + r9.toString());
//
//        KuzuQueryResult r10 = conn.query("COPY COMMENT_HAS_TAG_TAG FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/comment_hasTag_tag_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("COMMENT_HAS_TAG_TAG: " + r10.toString());
//
//        KuzuQueryResult r11 = conn.query("COPY COMMENT_IS_LOCATED_IN_PLACE FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/comment_isLocatedIn_place_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("COMMENT_IS_LOCATED_IN_PLACE: " + r11.toString());
//
//        KuzuQueryResult r12 = conn.query("COPY COMMENT_REPLY_OF_COMMENT FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/comment_replyOf_comment_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("COMMENT_REPLY_OF_COMMENT: " + r12.toString());
//
//        KuzuQueryResult r13 = conn.query("COPY COMMENT_REPLY_OF_POST FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/comment_replyOf_post_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("COMMENT_REPLY_OF_POST: " + r13.toString());



//        KuzuQueryResult r14 = conn.query("COPY FORUM_CONTAINER_OF_POST FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/forum_containerOf_post_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("FORUM_CONTAINER_OF_POST: " + r14.toString());
//        KuzuQueryResult r15 = conn.query("COPY FORUM_HAS_MEMBER FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/forum_hasMember_person_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("FORUM_HAS_MEMBER: " + r15.toString());
//        KuzuQueryResult r16 = conn.query("COPY FORUM_HAS_MODERATOR FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/forum_hasModerator_person_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("FORUM_HAS_MODERATOR: " + r16.toString());
//        KuzuQueryResult r17 = conn.query("COPY FORUM_HAS_TAG_TAG FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/forum_hasTag_tag_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("FORUM_HAS_TAG_TAG: " + r17.toString());

//        KuzuQueryResult r18 = conn.query("COPY ORGANISATION_IS_LOCATED_IN_PLACE FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/organisation_isLocatedIn_place_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("ORGANISATION_IS_LOCATED_IN_PLACE: " + r18.toString());

//        KuzuQueryResult r19 = conn.query("COPY PERSON_HAS_INTEREST_TAG FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_hasInterest_tag_0_0.csv' (HEADER=true, DELIM='|')");
//
//        System.out.println("PERSON_HAS_INTEREST_TAG: " + r19.toString());
//
//        KuzuQueryResult r20 = conn.query("COPY PERSON_IS_LOCATED_IN_PLACE FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_isLocatedIn_place_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("PERSON_IS_LOCATED_IN_PLACE: " + r20.toString());
//
//        KuzuQueryResult r21 = conn.query("COPY PERSON_KNOWS_PERSON FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_knows_person_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("PERSON_KNOWS_PERSON: " + r21.toString());
//
//        KuzuQueryResult r22 = conn.query("COPY PERSON_LIKES_COMMENT FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_likes_comment_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("PERSON_LIKES_COMMENT: " + r22.toString());
//
//        KuzuQueryResult r23 = conn.query("COPY PERSON_LIKES_POST FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_likes_post_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("PERSON_LIKES_POST: " + r23.toString());
//
//        KuzuQueryResult r24 = conn.query("COPY PERSON_STUDY_AT_ORGANISATION FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_studyAt_organisation_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("PERSON_STUDY_AT_ORGANISATION: " + r24.toString());
//
//        KuzuQueryResult r25 = conn.query("COPY PERSON_WORK_AT_ORGANISATION FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_workAt_organisation_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("PERSON_WORK_AT_ORGANISATION: " + r25.toString());
//
//        KuzuQueryResult r26 = conn.query("COPY PLACE_IS_PART_OF FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/place_isPartOf_place_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("PLACE_IS_PART_OF: " + r26.toString());


//         KuzuQueryResult r27 = conn.query("COPY POST_HAS_CREATOR_PERSON FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/post_hasCreator_person_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("POST_HAS_CREATOR_PERSON: " + r27.toString());
//
//        KuzuQueryResult r28 = conn.query("COPY POST_HAS_TAG_TAG FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/post_hasTag_tag_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("POST_HAS_TAG_TAG: " + r28.toString());
//
//        KuzuQueryResult r29 = conn.query("COPY POST_IS_LOCATED_IN_PLACE FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/post_isLocatedIn_place_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("POST_IS_LOCATED_IN_PLACE: " + r29.toString());


//        KuzuQueryResult r30 = conn.query("COPY HAS_TYPE FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/tag_hasType_tagclass_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("HAS_TYPE: " + r30.toString());
//
//        KuzuQueryResult r31 = conn.query("COPY TAGCLASS_IS_SUBCLASS_OF FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/tagclass_isSubclassOf_tagclass_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("TAGCLASS_IS_SUBCLASS_OF: " + r31.toString());

//-------------------------------------
//       create a copy query to copy the data to the TAG_HAS_TYPE_TAGCLASS table from the tag_hasType_tagclass_0_0.csv file







//        KuzuQueryResult r1 = conn.query("COPY CONTAINER_OF FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/forum_containerOf_post_0_0.csv' (HEADER=true)");
//        System.out.println("containerOf: " + r1.toString());
//
//        KuzuQueryResult r2 = conn.query("COPY HAS_CREATOR FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/new_comment_hasCreator_person.csv' (HEADER=true)");
//        System.out.println("hasCreator: " + r2.toString());
////COPY User FROM "user.csv" (HEADER=true, DELIM="|");
//        KuzuQueryResult r3 = conn.query("COPY HAS_INTEREST FROM '/Users/owenlipchitz/Owen/programming/social_network-sf0.1-CsvComposite-LongDateFormatter/flattenedData/person_hasInterest_tag_0_0.csv' (HEADER=true, DELIM='|')");
//        System.out.println("hasInterest: " + r3.toString());

//        KuzuQueryResult r4 = conn.query("CREATE EDGE hasMember (source, destination) FROM Forum TO Person TYPE 'D'");
//        System.out.println("hasMember: " + r4.toString());
//
//        KuzuQueryResult r5 = conn.query("CREATE EDGE hasModerator (source, destination) FROM Forum TO Person TYPE 'D'");
//        System.out.println("hasModerator: " + r5.toString());
//
//        KuzuQueryResult r6 = conn.query("CREATE EDGE hasTag (source, destination) FROM Message TO Tag TYPE 'D'");
//        System.out.println("hasTag (Message): " + r6.toString());
//
//        KuzuQueryResult r7 = conn.query("CREATE EDGE hasTag (source, destination) FROM Forum TO Tag TYPE 'D'");
//        System.out.println("hasTag (Forum): " + r7.toString());
//
//        KuzuQueryResult r8 = conn.query("CREATE EDGE hasType (source, destination) FROM Tag TO TagClass TYPE 'D'");
//        System.out.println("hasType: " + r8.toString());
//
//        KuzuQueryResult r9 = conn.query("CREATE EDGE isLocatedIn (source, destination) FROM Company TO Country TYPE 'D'");
//        System.out.println("isLocatedIn (Company): " + r9.toString());
//
//        KuzuQueryResult r10 = conn.query("CREATE EDGE isLocatedIn (source, destination) FROM Message TO Country TYPE 'D'");
//        System.out.println("isLocatedIn (Message): " + r10.toString());
//
//        KuzuQueryResult r11 = conn.query("CREATE EDGE isLocatedIn (source, destination) FROM Person TO City TYPE 'D'");
//        System.out.println("isLocatedIn (Person): " + r11.toString());
//
//        KuzuQueryResult r12 = conn.query("CREATE EDGE isLocatedIn (source, destination) FROM University TO City TYPE 'D'");
//        System.out.println("isLocatedIn (University): " + r12.toString());
//
//        KuzuQueryResult r13 = conn.query("CREATE EDGE isPartOf (source, destination) FROM City TO Country TYPE 'D'");
//        System.out.println("isPartOf (City): " + r13.toString());
//
//        KuzuQueryResult r14 = conn.query("CREATE EDGE isPartOf (source, destination) FROM Country TO Continent TYPE 'D'");
//        System.out.println("isPartOf (Country): " + r14.toString());
//
//        KuzuQueryResult r15 = conn.query("CREATE EDGE isSubclassOf (source, destination) FROM TagClass TO TagClass TYPE 'D'");
//        System.out.println("isSubclassOf: " + r15.toString());
//
//        KuzuQueryResult r16 = conn.query("CREATE EDGE knows (source, destination) FROM Person TO Person TYPE 'U'");
//        System.out.println("knows: " + r16.toString());
//
//        KuzuQueryResult r17 = conn.query("CREATE EDGE likes (source, destination) FROM Person TO Message TYPE 'D'");
//        System.out.println("likes: " + r17.toString());
//
//        KuzuQueryResult r18 = conn.query("CREATE EDGE replyOf (source, destination) FROM Comment TO Message TYPE 'D'");
//        System.out.println("replyOf: " + r18.toString());
//
//        KuzuQueryResult r19 = conn.query("CREATE EDGE studyAt (source, destination) FROM Person TO University TYPE 'D'");
//        System.out.println("studyAt: " + r19.toString());
//
//        KuzuQueryResult r20 = conn.query("CREATE EDGE workAt (source, destination) FROM Person TO Company TYPE 'D'");
//        System.out.println("workAt: " + r20.toString());



    /*


        KuzuQueryResult r1 = conn.query("COPY Post FROM './dataset/small/new_posts2.csv' (HEADER=true)");
        System.out.println("Post: " + r1.toString());

        KuzuQueryResult r2 = conn.query("COPY Tag FROM './dataset/small/new_tags.csv' (HEADER=true)");
        System.out.println("Tag: " + r2.toString());

        KuzuQueryResult r3 = conn.query("COPY User FROM './dataset/small/new_users3.csv' (HEADER=true)");
        System.out.println("User: " + r3.toString());

        KuzuQueryResult r4 = conn.query("COPY PARENT_OF FROM './dataset/small/new_posts_rel2.csv' (HEADER=true)");
        System.out.println("Parent: " + r4.toString());

        KuzuQueryResult r5 = conn.query("COPY HAS_TAG FROM './dataset/small/new_tags_posts_rel3.csv' (HEADER=true)");
        System.out.println("Has_tag: " + r5.toString());

        KuzuQueryResult r6 = conn.query("COPY POSTED FROM './dataset/small/new_users_posts_rel3.csv' (HEADER=true)");
        System.out.println("Posted: " + r6.toString());

           */
    }

    public static void cmdLine() throws KuzuObjectRefDestroyedException {
        Scanner cli = new Scanner(System.in);
        System.out.println("Kuzu DB interface");
        String query = "";

        while (true) {
            System.out.print(">>> ");
            query = cli.nextLine();
            if (query.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye");
                break;
            }
            System.out.println("Executing " + query);
            executeQuery(query);
        }
        cli.close();
    }

    public static void printResults(KuzuQueryResult result) throws KuzuObjectRefDestroyedException {
        System.out.println("Num tuples: " + result.getNumTuples());
        int count = 0;
        while (result.hasNext()) {
            KuzuFlatTuple row = result.getNext();
            System.out.print("Row " + count++ + " ");
            for (int i = 0; i<result.getNumColumns(); i++) {
                System.out.print(row.getValue(i).getDataType().getID() + ":" + row.getValue(i).toString() + ",");
            }
            System.out.println();
        }
    }

    public static void executeQuery(String query) throws KuzuObjectRefDestroyedException {
        // Execute a simple query.
        KuzuQueryResult result = conn.query(query);
        if (result == null || !result.isSuccess()) {
            System.out.println("ERROR");
            if (result != null) System.out.println(result.getErrorMessage());
            return;
        }

        System.out.println("Num tuples: " + result.getNumTuples());
        System.out.println("Execution time : " + result.getQuerySummary().getExecutionTime());
        int count = 0;
        while (result.hasNext())  {
            KuzuFlatTuple row = result.getNext();
            System.out.print("Row: " + row.getValue(0).getDataType().getID() + ":"+  row);
//            System.out.print("Row: " + row);
            row.destroy();
            count ++;
        }
        result.destroy();
    }

    public static String[] createNodeView(String query) throws KuzuObjectRefDestroyedException {
        KuzuQueryResult result_view = conn.query(query);
        System.out.println("Num tuples: " + result_view.getNumTuples());
        KuzuValue value = result_view.getNext().getValue(0);
        String[] resultList = value.toString().substring(1, value.toString().length()-1).split(",");
        String[] view_ids = Arrays.stream(resultList)
                .map(element -> "\"" + element + "\"")
                .toArray(String[]::new);
        //System.out.println(value.getDataType().getID());
        //System.out.println(Arrays.toString(resultList));
        result_view.destroy();
        return view_ids;
    }

    public static String[][] createPathView(String query) throws KuzuObjectRefDestroyedException {
        KuzuQueryResult result_view = conn.query(query);
        System.out.println("Num tuples: " + result_view.getNumTuples());
        KuzuFlatTuple tuple = result_view.getNext();
        KuzuValue nodesVal = tuple.getValue(0);
        KuzuValue relsVal = tuple.getValue(1);

        String[] nodesList = nodesVal.toString().substring(1, nodesVal.toString().length()-1).split(",");
        String[] relsList = relsVal.toString().substring(1, relsVal.toString().length()-1).split(",");
        String[] nodes_ids = Arrays.stream(nodesList)
                .map(element -> "\"" + element + "\"")
                .toArray(String[]::new);
        String[] relsIds = Arrays.stream(relsList)
                .map(element -> "\"" + element + "\"")
                .toArray(String[]::new);
        result_view.destroy();
        return new String[][] {nodes_ids, relsIds};
    }

    public static void testRelsInPath() throws KuzuObjectRefDestroyedException {
        String query = "MATCH p=(n:User)-[po:POSTED]->(post:Post) WHERE n.userId < 5 RETURN p";

        System.out.println(query);
        KuzuQueryResult result = conn.query(query);
        System.out.println("Num tuples: " + result.getNumTuples());
        for (int i = 0; i < result.getNumColumns(); i++) System.out.println(result.getColumnName(i));
        while (result.hasNext()) {
            KuzuFlatTuple row = result.getNext();
            // row[0] is recursive rel
            KuzuValue val = row.getValue(0);
            // rels is a VAR_LIST
            KuzuValue rels = KuzuValueRecursiveRelUtil.getRelList(val);
            // elements in rels are REL
            for (int j = 0; j < KuzuValueListUtil.getListSize(rels); j++) {
                KuzuValue rel = KuzuValueListUtil.getListElement(rels, j);
                System.out.println(KuzuValueRelUtil.toString(rel));
            }

            row.destroy();
        }
        result.destroy();
    }

    public static void testPreparedStatement() throws KuzuObjectRefDestroyedException {
        KuzuPreparedStatement prepStatement = conn.prepare("MATCH (n) WHERE ID(n) = $v1 or ID(n) = $v2 RETURN n");
        if (!prepStatement.isSuccess()) {
            System.out.print("Problem: ");
            System.out.println(prepStatement.getErrorMessage());
        }
        KuzuValue v1 = new KuzuValue(new KuzuInternalID(2,23585) );
        Map<String, KuzuValue> map = new HashMap<>();
        map.put("v1", v1);
        map.put("v2", new KuzuValue(new KuzuInternalID(2, 6490)));
        KuzuQueryResult result = conn.execute(prepStatement, map);
        if (!result.isSuccess()) {
            System.out.println(result.getErrorMessage());
        } else {
            System.out.println(result.getQuerySummary().getExecutionTime());
            printResults(result);
        }

        result.destroy();
    }

    public static void testAlternativeId() throws KuzuObjectRefDestroyedException {
        String query = "MATCH (n:Post)-[r:PARENT_OF]-(m:Post) WHERE n.postId < 10 RETURN LABEL(r), OFFSET(ID(r))";
        KuzuQueryResult result = conn.query(query);
        System.out.println(result.getQuerySummary().getExecutionTime());
        printResults(result);

        query = "MATCH (n:Post)-[r:PARENT_OF]-(m:Post) WHERE n.postId < 10 RETURN ID(r)";
        result = conn.query(query);
        System.out.println(result.getQuerySummary().getExecutionTime());
        printResults(result);

        query = "MATCH (n) WHERE list_contains([\"2:23585\",\"2:6490\",\"2:6491\",\"2:22258\",\"2:14629\",\"2:23583\",\"2:22252\",\"2:597\",\"2:596\",\"2:10263\"], string(ID(n))) RETURN n";
        result = conn.query(query);
        System.out.println(result.getQuerySummary().getExecutionTime());
        printResults(result);

        query = "MATCH (n) WHERE LABEL(n)=\"User\" AND list_contains([23585, 6490, 6491, 22258, 14629, 23583, 22252, 597, 596, 10263], OFFSET(ID(n))) RETURN n";
        result = conn.query(query);
        System.out.println(result.getQuerySummary().getExecutionTime());
        printResults(result);

        query = "MATCH p=(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE betterPost.postId < 10 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 "+
                " RETURN properties(NODES(p), \"_label\"),properties(NODES(p), \"_id\"),  properties(RELS(p), \"_label\"), properties(RELS(p), \"_id\")";
        result = conn.query(query);
        if (!result.isSuccess()) {
            System.out.println(result.getErrorMessage());
        } else {
            System.out.println(result.getQuerySummary().getExecutionTime());
            printResults(result);
        }

        query = "MATCH p=(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE betterPost.postId < 10 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 "+
                " RETURN properties(NODES(p), \"_id\"),  properties(RELS(p), \"_id\")";
        result = conn.query(query);
        if (!result.isSuccess()) {
            System.out.println(result.getErrorMessage());
        } else {
            System.out.println(result.getQuerySummary().getExecutionTime());
            printResults(result);
        }
    }


    public static void testCreatingKuzuValue() throws KuzuObjectRefDestroyedException {
        KuzuValue val = new KuzuValue("hi");
        System.out.println(val.getDataType().getID());
    }

    /**
     * Function to manually test certain queries
     */
    public static void test() throws KuzuObjectRefDestroyedException{
        String query = "";
        String view1 = "MATCH (n:User) WHERE n.userId < 10 RETURN COLLECT(ID(n))";
        //String[] view1_ids = createNodeView(view1);

        String view2 = "MATCH (n:User) WHERE n.userId > 3 RETURN COLLECT(ID(n))";
        //String[] view2_ids = createNodeView(view2);

        /*String pathview1 = "MATCH p=(n:User)-[po:POSTED]->(post:Post) WHERE n.userId < 5 " +
                "UNWIND(properties(NODES(p), \"_id\")) as x " +
                "UNWIND (properties(RELS(p), \"_id\")) as y  " +
                "RETURN COLLECT(DISTINCT x), COLLECT(DISTINCT y)";
        String[][] pathview1_ids = createPathView(pathview1);
        String[] pathview1_nodes = pathview1_ids[0];
        String[] pathview1_rels = pathview1_ids[1];
        System.out.println(Arrays.toString(pathview1_rels));
        System.out.println(Arrays.toString(pathview1_nodes));*/


        String pathview2 = "MATCH p=(n:User)-[:POSTED]-(po:Post) WHERE n.reputation > 8000 RETURN p";
        //printResults(conn.query(pathview2));

        /*
        // Local - 1 node view
        query = "MATCH (n:User) WHERE n.reputation < 300 AND " +
        "list_contains(" + Arrays.toString(view1_ids) + ", string(ID(n))) " +
        "RETURN n";


        // Local - 2 node views
        query = "MATCH (n:User) WHERE n.reputation < 300 AND " +
                "list_contains(" + Arrays.toString(view1_ids) + ", string(ID(n))) " +
                " AND list_contains(" + Arrays.toString(view2_ids) + ", string(ID(n))) " +
                "RETURN n";


        // Local - 2 views, 1 node, 1 path view
        query = "MATCH (n)-[r]-(m) WHERE " +
                "list_contains(" + Arrays.toString(view1_ids) + ", string(ID(n))) " +
                " AND list_contains(" + Arrays.toString(pathview1_nodes) + ", string(ID(m))) " +
                " AND list_contains(" + Arrays.toString(pathview1_rels) + ", string(ID(r))) " +
                "RETURN n";


        // Global - 1 node view
        query = "MATCH (n)-[:POSTED]-(p:Post) WHERE " +
                "list_contains(" + Arrays.toString(view1_ids) + ", string(ID(n))) " +
                "RETURN p";


        // Global - 1 path view
        query = "MATCH (p:Post)-[:HAS_TAG]-(m) WHERE " +
                "list_contains(" + Arrays.toString(pathview1_nodes) + ", string(ID(p))) " +
                "RETURN m";

         */

        // Global - 2 views, 1 node, 1 path
        /*query = "MATCH (u)-[:POSTED]-(p:Post)-[:HAS_TAG]-(t) WHERE " +
                "list_contains(" + Arrays.toString(pathview1_nodes) + ", string(ID(p))) AND " +
                "list_contains(" + Arrays.toString(view2_ids) + ", string(ID(u))) " +
                "RETURN t";
        */
        // query = "UNWIND [[\"5:1091\"],[\"5:1090\"],[\"5:3272\"],[\"5:3273\"]] AS edges MATCH p=(n)-[r]-(m) WHERE string(ID(r)) = list_element(edges, 1) RETURN p";
        //System.out.println(query);
        /*KuzuQueryResult result = conn.query(query);
        if (!result.isSuccess()) {
            System.out.println(result.getErrorMessage());

        } else {
            System.out.println("Num tuples: " + result.getNumTuples());
            while (result.hasNext()) {
                KuzuFlatTuple row = result.getNext();
                System.out.print("Row: " + row);
                //System.out.println(row.getValue(0).getDataType().getID());
                row.destroy();
            }
            result.destroy();
        }
        */
        testAlternativeId();

    }

}
