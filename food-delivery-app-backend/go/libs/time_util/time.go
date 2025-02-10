package time_util

import "time"

func GetCurrentTime() time.Time {
	return time.Now().UTC().Truncate(time.Second)
}

func Truncate(t time.Time) time.Time {
	return t.Truncate(time.Second).UTC()
}

func CompareTime(t1, t2 time.Time) bool {
	return t1.Truncate(time.Second).UTC() == t2.Truncate(time.Second).UTC()
}
