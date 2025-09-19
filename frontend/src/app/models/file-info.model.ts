export interface FileInfo {
    fileName: string;
    valid: boolean;
    identifier: string;
    recordingDate: string;
    patientName: string;
    channelNames: string[];
    recordingLengthSeconds: number;
    numberOfAnnotations: number;
}