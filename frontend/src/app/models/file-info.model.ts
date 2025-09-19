export interface FileInfo {
    fileName: string;
    isValid: boolean;
    identifier: string;
    recordingDate: string;
    patientName: string;
    numberOfChannels: number;
    channelLabels: string[];
    recordingLengthSeconds: number;
    numberOfAnnotations: number;
}