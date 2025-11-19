import streamlit as st
import requests
from requests.auth import HTTPBasicAuth
import pandas as pd

# --------------------------
#  Config（EC2 API）
# --------------------------
API_BASE = "http://localhost:8080/api"
USERNAME = "masaya"
PASSWORD = "password"

st.set_page_config(page_title="Plant Dashboard", layout="wide")
st.title("🌱 Plant Watering Dashboard / 植物澆水管理")

# --------------------------
#  Fetch functions
# --------------------------
def get_plants():
    url = f"{API_BASE}/plants"
    r = requests.get(url, auth=HTTPBasicAuth(USERNAME, PASSWORD))
    if r.status_code == 200:
        return r.json()
    return None


def add_plant(name, species, interval):
    url = f"{API_BASE}/plants"
    payload = {
        "name": name,
        "species": species,
        "waterIntervalDays": interval
    }
    r = requests.post(
        url,
        json=payload,
        auth=HTTPBasicAuth(USERNAME, PASSWORD)
    )
    return r.status_code == 200 or r.status_code == 201


def get_logs():
    url = f"{API_BASE}/watering/logs"
    r = requests.get(url, auth=HTTPBasicAuth(USERNAME, PASSWORD))
    if r.status_code == 200:
        return r.json()
    return None


# --------------------------
#  API functions
# --------------------------
def api_get(path):
    try:
        r = requests.get(f"{API_BASE}{path}", auth=HTTPBasicAuth(USERNAME, PASSWORD))
        if r.status_code == 200:
            return r.json()
    except:
        return None
    return None


def api_post(path, payload):
    r = requests.post(
        f"{API_BASE}{path}",
        json=payload,
        auth=HTTPBasicAuth(USERNAME, PASSWORD)
    )
    return r.status_code in (200, 201)


# --------------------------
#  Tabs layout
# --------------------------
tab1, tab2, tab3, tab4, tab5 = st.tabs(
    ["🌿 Plants 植物列表",
     "➕ Add Plant 新增植物",
     "💧 Water Logs 澆水紀錄",
     "⏰ Due Today 今日需澆水",
     "⚙️ Admin 管理"]
)

# ------------------------------------------------------------
# TAB1 — Plants list
# ------------------------------------------------------------
def water_now(plant_id, note=""):
    url = f"{API_BASE}/plants/{plant_id}/water"
    params = {"note": note} if note else {}
    r = requests.post(url, params=params, auth=HTTPBasicAuth(USERNAME, PASSWORD))
    return r.status_code == 200

with tab1:
    st.subheader("🌿 現在の植物一覧（Plants List）")

    plants = get_plants()

    if plants:
        for p in plants:
            col1, col2 = st.columns([3, 1])
            with col1:
                st.write(f"**{p['name']}** — ID {p['id']} — every {p['waterIntervalDays']} days")

            with col2:
                note_key = f"note_{p['id']}"
                note = st.text_input(f"Note (optional) for {p['id']}", key=note_key)

                if st.button(f"💧 Water now（立即澆水） — ID {p['id']}", key=f"water_{p['id']}"):
                    ok = water_now(p["id"], note)
                    if ok:
                        st.success(f"Watered plant {p['id']} successfully!")
                    else:
                        st.error(f"Failed to water plant {p['id']}.")


# ------------------------------------------------------------
# TAB2 — Add Plant
# ------------------------------------------------------------
with tab2:
    st.subheader("➕ Add New Plant / 新增植物")

    name = st.text_input("Name / 名稱")
    species = st.text_input("Species / 種類")
    interval = st.number_input("Water Interval Days / 澆水間隔（日）", 1, 60, 7)

    if st.button("Add / 新增"):
        if not name.strip() or not species.strip():
            st.warning("Name and species required / 名稱與種類不可空白")
        else:
            ok = api_post("/plants", {
                "name": name,
                "species": species,
                "waterIntervalDays": interval
            })
            if ok:
                st.success("Added successfully / 新增成功")
            else:
                st.error("Failed to add / 新增失敗")

# ------------------------------------------------------------
# TAB3 — Watering Logs
# ------------------------------------------------------------
with tab3:
    st.subheader("💧 Watering Logs / 澆水紀錄")

    logs = api_get("/watering/logs")

    if logs is None or len(logs) == 0:
        st.info("No logs / 尚無紀錄")
    else:
        df_logs = pd.DataFrame(logs)
        st.dataframe(df_logs)

        # --- Graph ---
        if "wateredAt" in df_logs.columns or "watered_at" in df_logs.columns:

            ts_col = "wateredAt" if "wateredAt" in df_logs.columns else "watered_at"

            df_logs[ts_col] = pd.to_datetime(df_logs[ts_col])
            df_logs = df_logs.sort_values(ts_col)

            # create index as water count
            df_logs["count"] = range(1, len(df_logs) + 1)

            st.line_chart(df_logs.set_index("count")[ts_col])


# ------------------------------------------------------------
# TAB4 — Due Today
# ------------------------------------------------------------
with tab4:
    st.subheader("⏰ Plants Due Today / 今日需澆水")

    today = api_get("/plants/due-today")
    if today is None:
        st.error("Cannot connect to API / 無法連線 API")
    elif len(today) == 0:
        st.info("No plants due today / 今日無需澆水植物")
    else:
        st.dataframe(pd.DataFrame(today))

# ------------------------------------------------------------
# TAB5 — Admin
# ------------------------------------------------------------
with tab5:
    st.subheader("⚙️ Admin Tools / 管理工具")

    errors = api_get("/admin/webhook-errors")

    if errors is None:
        st.error("Cannot load webhook errors / 無法載入錯誤紀錄")
    elif len(errors) == 0:
        st.info("No webhook error logs / 無錯誤")
    else:
        st.dataframe(pd.DataFrame(errors))




# import streamlit as st
# import requests
# from requests.auth import HTTPBasicAuth
# import pandas as pd

# # --------------------------
# #  Config（EC2 の API の URL）
# # --------------------------
# API_BASE = "http://localhost:8080/api"

# USERNAME = "masaya"
# PASSWORD = "password"


# st.set_page_config(page_title="Plant Dashboard", layout="wide")
# st.title("🌱 Plant Watering Dashboard (Streamlit)")

# # --------------------------
# #  Fetch functions
# # --------------------------
# def get_plants():
#     url = f"{API_BASE}/plants"
#     r = requests.get(url, auth=HTTPBasicAuth(USERNAME, PASSWORD))
#     if r.status_code == 200:
#         return r.json()
#     return None


# def add_plant(name, species, interval):
#     url = f"{API_BASE}/plants"
#     payload = {
#         "name": name,
#         "species": species,
#         "waterIntervalDays": interval
#     }
#     r = requests.post(
#         url,
#         json=payload,
#         auth=HTTPBasicAuth(USERNAME, PASSWORD)
#     )
#     return r.status_code == 200 or r.status_code == 201


# def get_logs():
#     url = f"{API_BASE}/watering/logs"
#     r = requests.get(url, auth=HTTPBasicAuth(USERNAME, PASSWORD))
#     if r.status_code == 200:
#         return r.json()
#     return None


# # --------------------------
# #  Layout
# # --------------------------

# tab1, tab2, tab3 = st.tabs(["🌿 Plants", "➕ Add Plant", "💧 Watering Logs"])

# # TAB 1 — Plants
# with tab1:
#     st.subheader("🌿 現在の植物一覧（Plants List）")

#     plants = get_plants()

#     if plants is None:
#         st.error("API 無法連線（cannot connect to API）")
#     else:
#         if len(plants) == 0:
#             st.info("目前沒有植物資料（No plants yet）")
#         else:
#             df = pd.DataFrame(plants)
#             st.dataframe(df)

# # TAB 2 — Add Plant
# with tab2:
#     st.subheader("➕ 植物を追加（Add a new plant）")

#     name = st.text_input("Name（名前）")
#     species = st.text_input("Species（種類）")
#     interval = st.number_input("Water Interval Days（間隔 日）", min_value=1, max_value=60, value=7)

#     if st.button("追加 / Add"):
#         if name.strip() == "" or species.strip() == "":
#             st.warning("Name / Species 不可為空")
#         else:
#             success = add_plant(name, species, interval)
#             if success:
#                 st.success("新增成功！（Added successfully）")
#             else:
#                 st.error("新增失敗（Add failed）")

# # TAB 3 — Logs
# with tab3:
#     st.subheader("💧 Watering Logs（澆水記錄）")

#     logs = get_logs()

#     if logs is None or len(logs) == 0:
#         st.info("目前沒有澆水記錄（No logs）")
#     else:
#         df_logs = pd.DataFrame(logs)

#         st.dataframe(df_logs)

#         if "wateredAt" in df_logs.columns:
#             df_logs["wateredAt"] = pd.to_datetime(df_logs["wateredAt"])
#             df_logs = df_logs.sort_values("wateredAt")

#             st.line_chart(df_logs.set_index("wateredAt")["plantId"])
