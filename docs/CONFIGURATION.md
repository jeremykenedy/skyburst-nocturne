# Configuration

Open Skyburst Nocturne from the device app list and use the D-pad to choose a setting. Changes apply the next time the preview or screensaver starts. Each visual choice includes Random. Randomize All resolves the settings again at each session start.

| Provider key | Options | Default | Effect |
| --- | --- | --- | --- |
| `density` | `sparse`, `balanced`, `dense`, `packed`, `random` | `balanced` | Number of background stars. |
| `motion` | `slow`, `natural`, `fast`, `random` | `natural` | Animation speed for launch and falling spark trails. |
| `rate` | `quiet`, `steady`, `festival`, `random` | `steady` | Average spacing between launches. Bursts may overlap. |
| `size` | `fine`, `natural`, `grand`, `random` | `natural` | Burst radius and launch trail scale. |
| `palette` | `aurora`, `ember`, `tropical`, `random` | `aurora` | Firework color family. |
| `brightness` | `night`, `dusk`, `bright`, `random` | `dusk` | Star and firework luminance. |
| `randomize_all` | `true`, `false` | `false` | Choose all six visual settings for each session. |

The app exposes the versioned settings provider at `content://com.jeremykenedy.skyburstnocturne.settings/schema` and `/settings`. The schema cursor contains `key`, `title`, `type`, `default`, `choices`, and `randomAllowed`. The settings cursor contains the saved key/value pairs. A host can update one setting by writing `key` and `value` to `/settings`; unsupported keys or values are rejected. Settings are local and this provider exposes only the app's visual preferences.

Random choices are resolved when a session begins and are not written back to the saved preferences. Brightness and density do not change dynamically until the next session.
