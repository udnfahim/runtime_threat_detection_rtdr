{{- define "rtdr.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "rtdr.fullname" -}}
{{- printf "%s-%s" (include "rtdr.name" .) .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
