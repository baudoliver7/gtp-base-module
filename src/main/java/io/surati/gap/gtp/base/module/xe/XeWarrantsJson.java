package io.surati.gap.gtp.base.module.xe;

import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.gtp.base.api.Warrant;
import io.surati.gap.payment.base.api.ReferenceDocument;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonStructure;
import org.takes.rs.RsJson;


public final class XeWarrantsJson implements RsJson.Source {

	private final Long count;
	
	private final Iterable<Warrant> items;
	
	final Double totalamount;
	
	final Double amountselected;
	
	final Double amountleft;
	
	public XeWarrantsJson(
		final Iterable<Warrant> items,
		final Long count,
		final Double totalamount,
		final Double amountselected,
		final Double amountleft
	) {
		this.count = count;
		this.items = items;
		this.totalamount = totalamount;
		this.amountleft = amountleft;
		this.amountselected = amountselected;
	}
	
	@Override
	public JsonStructure toJson() throws IOException {
		return Json.createObjectBuilder()
		   .add("items", toJson(this.items))
           .add("count", this.count)
           .add("total_amount_in_human", new FrAmountInXof(totalamount).toString())
           .add("total_amount", totalamount)
           .add("amount_left_in_human", new FrAmountInXof(amountleft).toString())
           .add("amount_left", amountleft)
           .add("amount_selected", amountselected)
           .add("amount_selected_in_human", new FrAmountInXof(amountselected).toString())
		   .build();
	}
	
	private static JsonArray toJson(final Iterable<Warrant> items) throws IOException {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (ReferenceDocument item : items) {
			builder.add(Json.createObjectBuilder()                
                .add("id", item.id())
                .add("name", String.format("%s N°%s", item.type().toString(), item.reference()))
				.add("amount", item.amount())
				.add("amount_in_human", new FrAmountInXof(item.amount()).toString())
				.add("advanced_amount", item.advancedAmount())
				.add("advanced_amount_in_human", new FrAmountInXof(item.advancedAmount()).toString())
				.add("amount_left", item.amountLeft())
				.add("amount_paid", item.amountPaid())
				.add("amount_left_in_human", new FrAmountInXof(item.amountLeft()).toString())
				.add("amount_paid_in_human", new FrAmountInXof(item.amountPaid()).toString())
				.add("date", item.date().format(DateTimeFormatter.ISO_DATE))
				.add("date_view", new FrShortDateFormat().convert(item.date()))
				.add("deposit_date", item.depositDate().format(DateTimeFormatter.ISO_DATE))
				.add("deposit_date_view", new FrShortDateFormat().convert(item.depositDate()))
				.add("entry_date", item.entryDate().format(DateTimeFormatter.ISO_DATE))
				.add("entry_date_view", new FrShortDateFormat().convert(item.entryDate()))
				.add("reference", item.reference())
				.add("object", item.object())
				.add("place", item.place())
				.add("status", item.status().toString())
				.add("status_id", item.status().name())
				.add("type", item.type().toString())
				.add("type_id", item.type().name())
				.add("step", item.step().toString())
				.add("step_id", item.step().name())
				.add("beneficiary", item.issuer().name())
				.add("abbreviated", item.issuer().abbreviated())
				.add("code", item.issuer().code())
           );
		}
		return builder.build();
	}
}
