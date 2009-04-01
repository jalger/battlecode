
package jj_nick;

import battlecode.common.Message;
import java.util.List;

/**
 * This class consists of static methods which encode and decode messages.
 * The basic problem with the way messages are set up now is that a robot
 * can only broadcast a single message in a round.  This is a very harsh
 * limitation because there is a lot of information to convey, and we often
 * want to convey more than one message per robot per turn.  For instance,
 * an archon might want to command his snipers to fall back, while also
 * forwarding a message it received from another unit; with the standadrd
 * message format we cannot send both at once.
 * Therefore we built a class that can pack multiple messages into one, with
 * a mechanism to both decode and encode various messages.
 * @author Nicholas Dunn
 * @date   February 28, 2009
 */
public class MessageFactory {




}