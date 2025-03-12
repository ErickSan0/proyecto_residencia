const admin = require("firebase-admin");
const functions = require("firebase-functions");

admin.initializeApp(); // 🔹 No es necesario pasar `serviceAccount`

exports.deleteUser = functions.https.onCall(async (data, context) => {
    if (!context.auth || !context.auth.token.admin) {
        throw new functions.https.HttpsError("permission-denied", "Solo los administradores pueden eliminar usuarios.");
    }

    const userId = data.userId;
    try {
        await admin.auth().deleteUser(userId);
        await admin.firestore().collection("usuarios").doc(userId).delete();
        return { success: true };
    } catch (error) {
        throw new functions.https.HttpsError("internal", "Error al eliminar usuario", error);
    }
});
