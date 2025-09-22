/**
 * Represents a communication channel.
 *
 * This interface defines the properties essential to a channel, such as its name and type.
 *
 * Properties:
 * - `name`: The name of the channel.
 * - `type`: The type of the channel (e.g., text, voice, etc.).
 */
export interface Channel {
    name: string;
    type: string;
}

/**
 * Represents information about a file and its associated metadata.
 * This interface is commonly used to describe the properties of a file
 * in a structured manner, including file details and associated data.
 *
 * @interface
 */
export interface FileInfo {
    fileName: string;
    valid: boolean;
    identifier: string;
    recordingDate: string;
    patientName: string;
    channels: Channel[];
    recordingLengthSeconds: number;
    numberOfAnnotations: number;
}