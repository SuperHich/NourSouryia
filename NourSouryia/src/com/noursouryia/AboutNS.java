package com.noursouryia;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.noursouryia.utils.NSFonts;

public class AboutNS extends Activity {

	private  ImageView back ;
	private  TextView text ,bsmla;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);

		back = (ImageView) findViewById(R.id.about_back);
		text = (TextView) findViewById(R.id.txv_about);
		bsmla = (TextView) findViewById(R.id.bsmla);

		text.setTypeface(NSFonts.getNoorFont());
		bsmla.setTypeface(NSFonts.getNoorFont());

		text.setText(System.getProperty("line.separator")+
				"( نور سورية ) نورٌ يضيء مع نسمات كلِّ صباحٍ مشرق من خلال هذه الثورة الوقادة الأخاذة، التي قامت لرفع الظلم والطغيان الذي عاني منه الشعب السوري عقوداً من الزمان، ولتحقيق العدل الذي قامت عليه السموات والأرض، وقدّم فيها الشعب السوري ولايزال التضحيات تلو التضحيات حتى تبلغ أهدافها قريبًا بإذن الله تعالى."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"ولشدة تفاؤلنا بقرب النصر وبزوغ الفجر، سمينا موقعنا ( نور سورية )، وهو موقع مستقل يهتم بالشأن السوري ومستجدات الواقع في سوريا."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"تشرف بهذا الإهداء المتواضع لشهدائنا وجرحانا ومعتقلينا ، ولأمهاتنا الصابرات المحتسبات، وللمرابطين والمجاهدين، ولشعب سورية الصابر الصامد، بكافة أطيافه، داخل الوطن وخارجه، لكي تلتقي جميع سواعد الوطن على نصرة الحق السليب المختطف من قبل النظام السوري."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"يهدف الموقع إلى أن يكون في قلب الثورة، عبر المتابعة النشطة لأهم الأخبار، ورصد الأحداث والمواقف، وانتقاء أهم المقالات والتحليلات والبيانات، واستكتاب الأقلام المهنية الرصينة، وتقديم التوجيه والتأصيل الذي تحتاجه الثورة، ومتابعة تقدّم الثورة ونجاحاتها، بحيث يكون مرجعاً متميزاً للثورة السورية وموجهاً لها."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"ويلتزم الموقع بالموضوعية والموثوقية، ويفتح أبوابه لكل فكرة ومقالة بنّاءة، ويرحب بكل نقد وتصحيح.. ويوقن أن هذه المنهجية هي الطريق لتحقيق ( نور سورية ) الذي سينبثق من رحم الظلام."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"( نور سورية) يوافيكم بالجديد مع إشراقة كل فجر إن شاء الله، ويعدكم بالمفيد لأجل (سورية).. ويتطلع أن يكون منكم وإليكم."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"رؤيتنا:"+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"الريادة في دراسة قضايا الثورة والشأن السوري، والارتقاء بالوعي السوري."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"رسالتنا:"+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"خدمة المهتمين بالشأن السوري، بتقديم محتوى متميز، يعالج قضايا الثورة والواقع السوري بمهنية وحرفية. ونشر الوعي بين أفراد المجتمع السوري."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"الشريحة المستهدفة:"+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"كافة أطياف الشعب السوري، داخل سورية أو خارجها."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"أهدافنا:"+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        تعزيز مسار ثورة الكرامة لكل أطياف الشعب السوري ضد الظلم والاستبداد."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        الإسهام في دعم الثورة السورية معلوماتياً وإعلامياً وإخبارياً واستراتيجياً."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        تقديم التأصيل الشرعي والتهذيب السلوكي للثورة لتحقق غاياتها ولا تنحرف عن مسارها."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        تفعيل التواصل مع النخب السورية داخل سورية وخارجها."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        رصد أهم قضايا الشأن السوري."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        خدمة الناشطين والإعلاميين والباحثين المهتمين بالشأن السوري."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        توفير أرشيف متكامل للشأن السوري وقضايا الثورة."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"·        خدمة النخب في بناء وتنمية سورية الجديدة."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"إن موقع ( نور سورية ) يسعى لاختيار الجيد من المقالات والأبحاث والكتب في أبوابها، كذلك فإنه يفتح باب التعليقات على المواد المنشورة، وذلك لإثراء الموقع بالمناقشات الهادفة، والإضافات المتميزة لدى زوار الموقع. وسيتم إدراج التعليق من قبل إدارة الموقع بعد اعتماده وتنسيقه. مع العلم أنه لن يتم إدراج أي تعليق لا يلتزم بأدب النصيحة والحوار.ويلزم من ذك أن ننوه إلى عدة نقاط:"+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"1.    الأبحاث والكتابات والتعليقات المنشورة تعبر عن رأي أصحابها، ولا تعبر بالضرورة عن رأي الموقع."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"2.    لا يمثل نشر بعض المقالات أو التعليقات أي تزكية لأصحابها، واختيارنا مقالة لكاتب معين لا يعني تزكية الكاتب أو بقية كتاباته."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"3.    تم اختيار بعض المواد بشيء من التصرف، وتمت الإشارة إلى ذلك في مواضعها."+System.getProperty("line.separator")+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"هذا الموقع يزداد ثراءً بمشاركاتكم وأطروحاتكم، فلا تتردد أخي الكاتب بإرسال مشاركتك إلى رابط المشاركات أو بريد الموقع."+System.getProperty("line.separator")+System.getProperty("line.separator")+
				"نسأل الله عز وجل أن يعجّل بالنصر والتمكين ، ويجعل سوريا عزيزة شامخة ويدحر كيد المعتدين. والحمد لله رب العالمين."+System.getProperty("line.separator"));
		
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				finish();

			}
		});


	}
}
