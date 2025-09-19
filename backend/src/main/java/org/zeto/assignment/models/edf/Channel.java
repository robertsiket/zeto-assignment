package org.zeto.assignment.models.edf;

/**
 * Represents a distinct channel in an EDF (European Data Format) file structure,
 * containing a name and type to identify and describe the channel.
 * <p>
 * Instances of Channel are used to define the properties of signals or data
 * originating from specific input sources within an EDF file.
 * <p>
 * This class is a record, providing an immutable and concise representation
 * with a compact syntax for declaring data-carrying classes.
 */
public record Channel(String name, String type) {
}
