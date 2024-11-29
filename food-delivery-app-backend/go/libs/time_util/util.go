package time_util

import "time"

func GetCurrentTime() time.Time {
	return time.Now().UTC().Truncate(time.Second)
}

func CompareTime(t1, t2 time.Time) bool {
	return t1.Truncate(time.Second).UTC() == t2.Truncate(time.Second).UTC()
}
