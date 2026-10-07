(function () {
            const el = document.getElementById('live-countdown');
            const label = document.getElementById('live-countdown-value');
            if (!el || !label) return;

            let remaining = parseInt(el.dataset.seconds, 10);
            const MAX_RETRIES = 6;                       // evita reload infiniti se qualcosa non va
            const RELOAD_DELAY_MS = 5000;                // tempo per lo scheduler (gira ogni 10s)

            function format(total) {
                const h = String(Math.floor(total / 3600)).padStart(2, '0');
                const m = String(Math.floor((total % 3600) / 60)).padStart(2, '0');
                const s = String(total % 60).padStart(2, '0');
                return h + ':' + m + ':' + s;
            }

            function reloadWithLimit() {
                let retries = parseInt(sessionStorage.getItem('liveRetries') || '0', 10);
                if (retries >= MAX_RETRIES) {
                    sessionStorage.removeItem('liveRetries');
                    return;                              // smette di riprovare
                }
                sessionStorage.setItem('liveRetries', String(retries + 1));
                setTimeout(() => location.reload(), RELOAD_DELAY_MS);
            }

            label.textContent = format(remaining);

            if (remaining <= 0) {
                label.textContent = 'in partenza...';
                reloadWithLimit();
                return;
            }

            const timer = setInterval(() => {
                remaining--;
                label.textContent = format(Math.max(remaining, 0));
                if (remaining <= 0) {
                    clearInterval(timer);
                    label.textContent = 'in partenza...';
                    reloadWithLimit();
                }
            }, 1000);
        })();